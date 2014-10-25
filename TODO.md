TODO
====

- Any change to `jobConfigs` and `classpath` files need to be reflected
  in the workspace/generated directories
- Use caching of inputs and outputs, only execute tasks when needed
- Keep directory structure of `jobConfigs` in workspace
- Only take the dsl files into account that are coming from `jobConfigs`,
  without custom file pattern matching like `**/*.dsl.groovy`
