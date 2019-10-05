# egit-extensions

This is an Eclipse plugin adding some features to Egit.

## Maven Project Import

The Git Repositories view's import project wizard is not very helpful for maven projects.
If you select specialized import wizards, you need to select the maven wizard but then the base folder is not pre-selected.

This plugin adds an Import Maven Project entry to the context menu (for the repository and the Working Tree).

# Steps to build and use the plugin

Currently, there is no update site. I have the egit ui source code checked out.
There should not be compile errors in the tooling.egit-plugin. Select Export Deployable plug-ins and fragments, and place the resulting jar in the dropins folder of your Eclipse installation.