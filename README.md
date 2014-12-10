# outpace-ex

Clojure implementation of the 'OCR' Outpace technical exercise.

Building a standalone executable jar file, or running from a clone of repository requires [Leiningen](http://leiningen.org).


## Building the standalone jar file.

All being well just invoke:

    $ lein uberjar

This will generate the jar file in the target subdirectory:

    target/outpace-ex-0.1.0-standalone.jar

## Usage

### lein run
To see example output on STDOUT run directly with leiningen from within the local repository:

    $ lein run test_files/valid_data.txt 

More generally to specify the output file use the --output option:

    $ lein run -- --output output_file input_file

See --help for a more verbose description:

    $ lein run -- --help 

### standalone jar invocation

Having built the standalone jar file with "lein uberjar" it can be called as below,
where JARFILE_PATH is "target/" if you are in the root of the repository:

    $ java -jar JARFILE_PATH/outpace-ex-0.1.0-standalone.jar

Options are the same as for "lein run" above e.g.:
	
    $ java -jar target/outpace-ex-0.1.0-standalone.jar test_files/valid_data.txt


## Documented source

 docs/uberdoc.html contains the [Marginalia](https://github.com/gdeer81/marginalia) generated documented source code. Unfortunately this isn't renderable directy from a Github private repository. Clone the repo and view it locally if you wish to see it.

## License

Copyright © 2014 Alexander Stoddard

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
