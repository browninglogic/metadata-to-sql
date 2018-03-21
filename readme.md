## Introduction

This application both imports music metadata (MP3, AAC, and anything else supported by jaudiotagger) into a SQL database and writes any changes made and marked in said database back to the music library.  The idea is to provide an interface through which to analyze and make bulk edits to the metadata of a large music library using the power of SQL queries.

## Status

This is a quick-and-dirty project that I wrote for fun.  As such, it works, but it's currently unpolished.  When my personal life allows me the time to do so, I fully intend to revisit this project and properly finish it.  The most notable things that I intend to do are:
* Implement proper error handling / logging
* Write proper unit tests
* Implement support for PostgreSQL

## Setup
1. Set up an instance of MySQL, if you don't already have one
2. Run the script included in the "sql" folder to set up the schema, table, and procedures
3. Import this repository to Eclipse, or your other IDE-of-choice
4. Import jaudiotagger from https://bitbucket.org/ijabz/jaudiotagger/branch/master.  You might have to locally delete some stuff to get it to compile.  Alternatively you could find and reference the *.jar version of the library.
