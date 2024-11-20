# Spring AI Recipe Finder

Demo on how easy it is to build AI-driven applications with Spring Boot and Spring AI. 
It also shows how to implement advanced techniques for the adaption of foundation models with Function Calling and RAG.

![](docs/images/ui-sample.png)

[Slides: Building AI-Driven Spring Applications with Spring AI](docs/SpringAI-From-exploration-to-production.pdf)

# Setup
## LLM
### Local LLM (Ollama)
As Ollama doesn't yet provide a text-to-image model, recipe image generation is not available with this setup.
Also Function calling is not supported by the Ollama models.
#### Option 1
1. Download and install Ollama on your local machine [link](https://ollama.com/)
2. Start llama3 model
    ```
    ollama run llama3
    ```
#### Option 2
By enabling the "ollama-compose" Spring profile, the llama3 model will be automatically started and configured with docker compose.
Depending on your system (e.g. ARM macs) this is not a recommended setup due to performance reasons.
```
export SPRING_PROFILES_ACTIVE=ollama-compose
```
### OpenAI
Set the API key via an environment variable or in [application-openai.yaml](src/main/resources/application-openai.yaml).
```
export SPRING_AI_OPENAI_API_KEY=<INSERT KEY HERE>
```
Run your application with the "openai" Spring Profile.
```
export SPRING_PROFILES_ACTIVE=openai
```

### Azure OpenAI
Set the API key and endpoint via environment variables or in [application-azure.yaml](src/main/resources/application-azure.yaml).
```
export SPRING_AI_AZURE_OPENAI_API_KEY=<INSERT KEY HERE>
export SPRING_AI_AZURE_OPENAI_ENDPOINT=https://{your-resource-name}.openai.azure.com
```

Make sure the deployment names of the models match exactly what's in your [application-azure.yaml](src/main/resources/application-azure.yaml) configuration file.

Currently, [**only some regions support image generation** with Dall-E](https://learn.microsoft.com/en-us/azure/ai-services/openai/concepts/models#dall-e-models).
If you use a region that doesn't support it, you have to disable the image generation by setting `ai.azure.openai.image.enabled: false` in the [application-azure.yaml](src/main/resources/application-azure.yaml) configuration file to not run into errors.

Run your application with the "azure" Spring Profile.
```
export SPRING_PROFILES_ACTIVE=azure
```

### Vector DB
On your local machine, a Redis database is automatically started and configured with docker compose.

# Running the application locally
```
./gradlew bootRun
```
Open [http://localhost:8080](http://localhost:8080) in your browser. 
Enter the ingredients (e.g. "Potatoes") you want to find a recipe for in the form and press the "find" button.

## Function Calling 
By checking the "Prefer available ingredients" checkbox, [Function Calling](https://docs.spring.io/spring-ai/reference/1.0/concepts.html#_function_calling) will be enabled.
As the functionalities to add always available ingredients and for the API call to check the available ingredients in the fridge are not yet implemented, they can be configured via the
`app.always-available-ingredients` and `app.available-ingredients-in-fridge` properties in [application.yaml](src/main/resources/application.yaml).

Bacon and onions are currently configured for available ingredients in fridge.
With the input "Potatoes", you should get a recipe with potatoes and bacon.
![](docs/images/ui-sample-function-calling.png)

## Retrieval-Augmented Generation(RAG)
By checking the "Prefer own recipes" checkbox, [Retrieval-Augmented Generation](https://docs.spring.io/spring-ai/reference/1.0/concepts.html#concept-rag) will be enabled.

To upload your own PDF documents for recipes to the vector database, there is a REST API endpoint implemented. 
```
curl -XPOST -F "file=@$PWD/Easy_recipes.pdf" http://localhost:8080/api/v1/recipes/upload
```
The sample recipe part of this repository is made with 14 recipe I want to promote. With the input "beef", you should get the beef in beer recipe.
![](docs/images/ui-sample-rag.png)

## MultiModal prompt feature
By checking the "Scan my dish" checkbox, [Multimodality](https://docs.spring.io/spring-ai/reference/api/multimodality.html) will be enabled. By adding a publicly available jpeg image of a dish(ie: https://img.cuisineaz.com/660x660/2013/12/20/i16509-photo-de-tartiflette.jpeg), you should get its recipe. 

## Chatbot feature
By default, chatbot is configured with the [simplest chatclient onliner](https://github.com/spuchol81/spring-ai/blob/main/src/main/java/com/example/ChefService.java#L34-L42) in streaming mode.
Give your llm the chef personality by uncommenting system prompt line. ![](docs/images/switchpersonality.png)
give your llm access to vector db content by uncommenting the advisor line.
If you ask for a beef recipe, you should get the Beef in Beer recipe. 