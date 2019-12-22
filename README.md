# egit-extensions

This is an Eclipse plugin adding some features to Egit.

## Maven Project Import

The Git Repositories view's import project wizard is not very helpful for maven projects.
If you select specialized import wizards, you need to select the maven wizard but then the base folder is not pre-selected.

This plugin adds an Import Maven Project entry to the context menu (for the repository and the Working Tree).

## open extern

We found the openextern plugin very useful.
With a very small modification, it also opens folders from the Repositories View working tree node.

## Bitbucket View

For self hosted bitbucket repositories, an additional view simplifies cloning.
The existing repositories are retrieved via API and presented in a view.
Cloning is invoked by context menu entry.
A view filter on the repository name allows to find the wanted repository quickly.

# Steps to build and use the plugin

Currently, there is no update site. In order to prevent dependency cycle problems, use a regular Eclipse (no Egit development setup).
There should not be compile errors in the tooling.egit-plugin. Select Export Deployable plug-ins and fragments, and place the resulting jar in the dropins folder of your Eclipse installation.

In order to build all features, you also need to clone https://github.com/nittka/openextern.git and switch to the egit-extensions branch.