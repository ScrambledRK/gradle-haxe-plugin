# gradle-haxe-plugin
Gradle Plugin to build Haxe Multi-Platform Projects; trying to follow the "intented way" (looking up the source for C, Java and Javascript  Plugins and Gradle documentation)

---

runtime:
* platform specific configuration possible 
* debug, production and custom configuration possible
* subproject direct code (module) or artifact (library) dependency possible

development-time:
* abstraction between haxe specific implementation and language agnostic
* useable abstraction layer for compliation and transformation steps

* Leaky Abstraction from gradle to plugin (model confuses with almost empty abstractions)
* confusing directory code (mixing gradle and custom schema)
* haxe compiler configuration "not the gradle way" (e.g. no model for configuration dsl)

