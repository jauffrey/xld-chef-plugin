## Requirements ##

* **XL Deploy requirements**
    * **XL Deploy**: Version 5.1.0+
    * **Chef**: Versions 10.1+ (Unix and Windows)
    * **Other XL Deploy plugins**: None

* **Infrastructural requirements**
    * `chef-solo` software version 10.1+ configured on a Unix or Windows machine

## Overview ##

The Chef plugin is an XL Deploy plugin that uses the Chef provisioning tool to install recipes on hosts.
See the **Features** section below for the supported features.

## Features ##

* Apply recipe for provisioning using `chef-solo`.

## Limitations ##

* The plugin only supports deployment using `chef-solo`.

## Release notes

### XL Deploy Chef plugin 5.1.0

Initial release.
