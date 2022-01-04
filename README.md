# meta

meta is a linux tool for performing git commands such as *clone*, *status*, *fetch* and *pull* at the same time
on multiple git repositories. All repositories belonging to a project are configured in a configuration file named
*meta.conf*. 

## Build

1. Clone [arthurpicht/meta](https://github.com/arthurpicht/meta.git).
2. Call 

        gradle fatJar

## Install

Add the `bin`-directory of the local arthurpicht/meta repository to your PATH variable.

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

An absolute path or a path relative to the meta directory. The denoted directory will be used as a reference for all following repository configurations.

Default value: `..` (parent directory of meta directory)
 
#### targets

A list of target names. Valid target names are `dev`, `prod` and all names beginning with either `dev-` or `prod-`.

Default: `dev`, `prod`

### Repository Sections

Each contained repository is configured by a section on its own. The name of the section can be chosen freely and must
be unique within the configuration file.

## Execution


