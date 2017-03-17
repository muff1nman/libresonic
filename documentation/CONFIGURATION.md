# Startup Configuration Guide

Libresonic has two sets of configuration. 
One is the configuration stored in the `libresonic.properties` file. The
other are parameters supplied as Java System Properties. Most configuration changes
can be done via the web interface on the Settings tab. These changes are stored
in the `libresonic.properties` file. However, there are a couple parameters that
cannot be changed via the interface. This document describes these 
parameters and how to configure them for Tomcat and Spring Boot.

## Java Parameters

#### `libresonic.home`
This parameter dictates the folder where libresonic will store its logs, 
settings, transcode binaries, index and database if using the default H2 
database. As such it is recommended to backup this folder.

*default: `/var/libresonic` or `C:\\music`*

#### Setting Java Parameters on Tomcat

#### Setting Java Parameters for Standalone Package (SpringBoot)

## Libresonic Properties

#### `DatabaseConfigType`
#### `DatabaseConfigEmbedDriver`
#### `DatabaseConfigEmbedUrl`
#### `DatabaseConfigEmbedUsername`
#### `DatabaseConfigEmbedPassword`
#### `DatatbaseMysqlMaxlength`
#### `DatabaseUsertableQuote`
#### `DatabaseConfigJNDIName`

#### Setting Libresonic Properties on Tomcat

#### Setting Libresonic Properties for Standalone Package (SpringBoot)
