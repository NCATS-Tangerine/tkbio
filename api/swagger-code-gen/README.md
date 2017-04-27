# SWAGGER CODE RE-GENERATION #

On occasion you may wish to comment out the `--skip-overwrite` flag in the `generateClient.sh` and `generateServer.sh` files, and re-generate the whole project while overwriting everything. The code that Swagger generates is not free of errors, and there are some things that you will have to manually fix (on top of re-implementing the API methods!). I suggest that you create a backup of the tkbio/api directory, so that you can copy and paste your previous work into the newly re-generated code.

## Swagger2SpringBoot ##

Open `tkbio/api/server/src/main/java/bio/knowledge/server/Swagger2SpringBoot.java`

This class will look something like this

```java
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "bio.knowledge.server")
public class Swagger2SpringBoot implements CommandLineRunner {
	...
}
```

You need to change the @ComponentScan annotation so as to include a reference to `bio.knowledge.database`.

```java
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {
		"bio.knowledge.server",
		"bio.knowledge.database"
})
public class Swagger2SpringBoot implements CommandLineRunner {
	...
}
```
