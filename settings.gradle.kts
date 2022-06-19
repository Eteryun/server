rootProject.name = "eteryun"

sequenceOf(
    "api",
    "launch",
    "example"
).forEach {
    include("eteryun-$it")
    project(":eteryun-$it").projectDir = file(it)
}
