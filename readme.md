## Introduction

This application aims to import music metadata (MP3, AAC, and anything else supported by jaudiotagger) into a SQL database, and then write any changes made and marked in that database back to the music library.  The idea is to provide an interface through which to analyze and modify the metadata of a large music library using the power of clever SQL queries, if you're so inclined.

The basic functionality works in the general case, but it's still a work-in-progress, as evidenced by the TODO comments throughout the code.

This application depends on the jaudiotagger library

## Setup
1. Set up an instance of MySQL, if you don't already have one
2. Run the script included in the "sql" folder to set up the schema, table, and procedures
3. Import this repository to Eclipse, or your other IDE-of-choice
4. Import jaudiotagger from https://bitbucket.org/ijabz/jaudiotagger/branch/master.  You might have to locally delete some stuff to get it to compile.  Alternatively you could find and reference the *.jar version of the library.

## Notes
The database schema consists of a single non-normalized flat table, where each tag field is represented as a varchar value.  This would be terrible database design in other scenarios, but in this instance it makes sense because the intention is to directly model the metadata of a large set of MP3 files, which themselves are non-normalized and made up of all string values.
