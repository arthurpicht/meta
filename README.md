# meta

meta is a linux tool for managing multi git projects. It allows for performing git commands such as *clone*, *status*, 
*fetch* and *pull* as bulk operations on all configured repositories. It produces concise output, in particular when
executing *status*.

The configuration of meta is done by a configuration file with name `meta.conf`. The idea is to store `meta.conf` in a git
repository on its own. When starting developing or deploying on a new location, only that single meta
repository needs to be cloned. All other repositories needed to be cloned will be handled by meta subsequently.

Meta is intentionally restricted to "reading" operations.

Meta is intentionally restricted to git repos that are either accessible via ssh by keys or via https anonymously.
Thus, authentication by username/password is not supported.

## Build

1. Make sure you have Java 11 and Gradle 6 installed.
2. Clone repository [arthurpicht/meta](https://github.com/arthurpicht/meta.git):

        git clone https://github.com/arthurpicht/meta.git
3. Call 

        gradle fatJar

## Install

Add the `bin` directory of the local arthurpicht/meta repository to your PATH variable.

Check the installation by calling:

    meta --version
 
## Definitions

Just to make some words clear...

`meta.conf`: name of the configuration file    
`meta directory`: the directory containing `meta.conf`

## Configuration

### Section [general]

Section [general] is optional. If not specified, all default values for respective configuration parameters are applied.

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

## Execution

## meta.conf binding strategy

`meta` executes exactly for one bound meta.conf file. When called, meta applies the following strategy for finding and
binding meta.conf:

If no other configuration is applied, meta will look for a meta.conf in the current working directory.

If the environment variable `META_DIR` is set, meta will look for a meta.conf file in the directory, which is
denoted by `META_DIR`. This overrides the default behaviour, meaning that it will always bind META_DIR/meta.conf, even
if a meta.conf file is existing in current working directory.

If `meta` is called with a global option `--metaDir {dir}` or `-d {dir}`, it will look for meta.conf in the specified
directory. This will override all other binding behaviours.

## meta commands

### meta clone

`meta clone` clones all configured repositories.

When cloning for the first time (initialization), the target has to be specified by `--target` specific option. All 
following calls of meta will be applied for that target. Changing the target after initialization is not provided.

### meta status

`meta status` shows a concise output for all configured repos.

### meta fetch

`meta fetch` will execute fetch on all repositories.

### meta pull

`meta pull` will execute pull on all repositories. It only executes if the particular repository has no unpushed
changes.
