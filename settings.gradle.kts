rootProject.name = "Aves"
/*
val ci = System.getenv().containsKey("CI")

buildCache {
    local {
        isEnabled = !ci
    }
    if (ci) {
        remote(HttpBuildCache::class) {
            url = uri(System.getenv("GRADLE_CACHE_URL"))
            isPush = ci
            isAllowUntrustedServer = true
            isAllowInsecureProtocol = true
            credentials {
                username = System.getenv("GRADLE_CACHE_USER")
                password = System.getenv("GRADLE_CACHE_PASSWORD")
            }
        }
    }
}*/
