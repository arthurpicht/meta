# meta

meta is a linux tool for managing multi git projects. It allows for performing git commands such as *clone*, *status*, 
*fetch* and *pull* as bulk operations on all configured repositories. It produces concise output, in particular when
executing *status*. Furthermore, meta manages switching between features affecting different repositories.  

The configuration of meta is done by a configuration file named `meta.conf`. The idea is to store `meta.conf` in a git
repository on its own. When starting developing or deploying on a new location, only that single meta
repository needs to be cloned. All subsequent clones and further git operations will then be handled by meta.

Meta does no harm. Except performing `fetch`, `pull`, and switching between branches, meta will not change the state of
the repo. 

Meta is restricted to git repos intentionally that are either accessible via ssh by keys or via https anonymously.
Thus, authentication by username/password is not supported.

## Install

Download the [latest release](https://github.com/arthurpicht/meta/releases) and put it on your $PATH.

Check the installation by calling:

    meta --version

If you want to build meta on your own, see paragraph *Build* below.
 
## Definitions

Just to make some words clear...

`meta.conf`: name of the configuration file to be created by the user    
`meta directory` or `META_DIR`: the directory containing `meta.conf`

## Configuration

### Section [general]

Section [general] is optional. If not specified, all default values for respective configuration parameters are applied.

It can contain the following configuration parameters.

#### referenceDir

An absolute path declaration or a path declaration relative to the meta directory. The denoted directory will be used
as a reference for all following repository configurations.

Default value is the parent directory of meta directory (`..`).
 
#### targets

A list of target names. Valid target names are `dev`, `prod` and all names beginning with either `dev-` or `prod-`.

Default: `dev`, `prod`

### Repository Sections

Each contained repository is configured by a section on its own. The name of the section can be chosen freely and must
be unique within the configuration file.

#### url

URL of git repository.

#### urlReadonly

Optional. URL of git repository for anonymous read access, like public open source repositories.
If target is of type `prod` and parameter `urlReadonly`is specified, given URL is bound for git operations.

Specifying this parameter allows CI/CD pipelines on production machines for accessing public git repositories
without granting access by ssh keys.

#### destinationDir

Optional. Default is [referenceDir](#referencedir). Local destination directory for git repository.
Specified as relative or absolute path. If specified as relative path, it will be evaluated relative to 
[referenceDir](#referencedir).

#### repoName

Optional. Default is the name of the repository as specified on remote.
If specified, target directory of git repository will be named accordingly.

#### branch

Optional. Default is default git branch.
If specified, repository will be checked out automatically for given branch after
performing `clone`.

#### target

Optional. Default are all targets specified by [parameter targets in general section](#targets).
List of targets. Subset of targets specified by [parameter targets in general section](#targets).
Meta will only include repository in execution if current target is included in list.

## Execution

## meta.conf binding strategy

`meta` executes exactly for one bound meta.conf file. When called, meta applies the following strategy for finding and
binding meta.conf:

If no other configuration is applied, meta will look for a meta.conf in the current working directory.

If the file `~/.meta/meta_dir` exists, meta will evaluate the first line of that file as an absolute path
to a directory (META_DIR) where it expects to find a meta.conf. This overrides the default behaviour, meaning that it
will always bind META_DIR/meta.conf, even if a meta.conf file is existing in current working directory.

If `meta` is called with a global option `--metaDir {dir}` or `-d {dir}`, it will look for meta.conf in the specified
directory. This will override all other binding behaviours.

## meta commands

### meta clone

`meta clone` clones all configured repositories.

When cloning for the first time (initialization), the target has to be specified by `--target` specific option. All 
following calls of meta will be applied for that target. Changing the target after initialization is not provided.

If `meta.conf` contains a `branch` declaration for the repository to be cloned, then the specified branch will be
checked out. Otherwise, the repo keeps at the respective default branch.

`meta clone` can be called subsequently, especially after adding further declarations to `meta.conf`. When doing so,
meta will check out the appropriate feature branch, if a feature is selected and the repo to be cloned contains a
branch related to that particular feature. In that case, a possible `branch` declaration for that repo in `meta.conf`
will be ignored.

On subsequent calls of `meta clone` repos that are already existing keep untouched and will be displayed as skipped.

### meta status

`meta status` shows a concise output for all configured repos.

### meta fetch

`meta fetch` will execute fetch on all repositories.

### meta pull

`meta pull` will execute pull on all repositories. It only executes if the particular repository has no unpushed
changes.

### meta feature

Working on features using feature branches can affect not only one but several repos. Meta can help with switching
between different features if

* all related feature-branches are named consistently over all affected repos and
* all created feature-branches begin with a `feature-` prefix.

`meta feature show` shows all features with affected repositories. A currently checked out feature is marked with
a `*`.

`meta feature checkout <featureName>` switches to a specified feature. The operation is not executed if any affected repo
has uncommitted changes. Using the specific option `--force` or `-f` will switch also on uncommitted changes as long
as those changes do not reflect modified files.

`meta feature reset` resets repos affected by currently checked out feature to base branch configuration. Feature
branches are reset by checking out those branches that were present after `meta clone`. This operation will not be executed, if any repo
has uncommitted changes. Using the specific option `--force` or `-f` will ignore that, as long as those changes do not
reflect modified files. Applying the specific option `--all` or `-a` will cause the operation to be executed not only
on repos related to the lastly checked out feature, but on all.

## rapidly changing into repo directories

Prerequisite: 

* [fzf](https://github.com/junegunn/fzf) is installed.
* `~/.meta/meta_dir` is configured [as described](#meta.conf-binding-strategy).
* `meta clone` is called at least one time before, and hence file `META_DIR/.meta-local/paths` exists.
* Source file `extras/bash_functions` from the cloned arthurpicht/meta repository within your `.bashrc` file. OR: Add the following snippet to users `.bashrc` file:
    ```bash
    function m() {
        meta_dir_file=~/.meta/meta_dir
        if [ ! -f "${meta_dir_file}" ]; then
            echo "File ${meta_dir_file} not found."
            return 1
        fi
      meta_paths="$(head -n 1 ~/.meta/meta_dir)/.meta-local/paths"
      if [ ! -f "$meta_paths" ]; then
          echo "File ${meta_path} not found."
          return 1
      fi
      dir=$(cat "${meta_paths}" | fzf --height 40% --layout=reverse)
      if [ ! -z "$dir" ]; then
          cd $dir
      else
          echo $? > /dev/null
      fi
    }
    ```

Calling `m` on command line (bash) allows you for selecting one out of all directories related to current meta project.
When selected, a `cd` is performed into chosen directory. 

The following directories are displayed for selection:
* all repository directories belonging to initialized target
* all parent directories of repository directories
* meta directory

## meta repo

As mentioned before, putting `meta.conf` into a repository on its own *("the
meta repo")* is considered a good idea. When doing so, include subdirectory
`.meta-local` into `.gitignore` as it is created by meta and
contains information about specific local circumstances.

## Build

If you want to build meta on your own, go like that:

1. Make sure you have Java 11 and Gradle 6 installed.
2. Clone repository [arthurpicht/meta](https://github.com/arthurpicht/meta.git):

        git clone https://github.com/arthurpicht/meta.git
3. Call

        gradle fatJar
4. Add the `bin` directory of the local arthurpicht/meta repository to your PATH variable.

## license

Apache-2.0 License
