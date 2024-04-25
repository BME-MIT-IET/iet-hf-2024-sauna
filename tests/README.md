# Hello, I've been updated 😄

My current information is based on the tester described in `10.pdf`.

## _To test or not to test?_

Write a test file with the `.pasta` extension\
**OR**\
Edit a config file (ending in `.config.pasta`) to include multiple test files.

## Check me some expected output?

Type `@<variableName>.<attributeName> = <expectedValue>` in your test file
(as if you were typing a `view` command) to make an assertion.\
Type `@<variableName>` to check for an object's existence _or_
`@!<variableName>` to check for its non-existence.

## I want muffins! 🧁

_Well, me too..._\
Change your working directory, so that you could successfully run `java macaroni.Main`,
and run **the test program** from here!\
Give your program the path of test or config files as args. _At least it'll get them muffin..._

&nbsp;

### Extra

- The tester will create its log files based on the path of the first argument it gets.
- Lines starting with a `#` are marked as commented. These will not be interpreted.
- Prefer using relative paths within your test files.
- Config files can also contain other nested configs.
  (This will only work if their paths are nested as well.)

&nbsp;

#### I wish you all a happy testing! 😊

&nbsp;
