#A vocal assistant for HSBC banking information build with Amazon Echo

## Concepts
This project is a MVP developed by students for HSBC
The assistant can answer private or public questions :
- Public ones: Information about HSBC agencies using the Google API
- Private ones: We use a postgres database to get information about the client

For the private questions, an authentification process has been set up.
## Setup
To run this example skill you need to do three things. The first is to deploy the example code in a Amazon Lambda's server, and the second is to configure the Alexa skill to use Lambda.
You will need a configuration file too for using Google API and the database.
### AWS Lambda Setup
1. Go to the [AWS Console](http://aws.amazon.com/lambda) and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda.
2. Click on the Create a Lambda Function or Get Started Now button.
3. Skip the blueprint
4. Name the Lambda Function "HSBC".
5. Select the runtime as Java 8
6. Create a file named config.properties in the folder src/main/resources. This file should be formatted as the example. You have to get an API key from Google and database's credentials. 
7. Go to the the root directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a zip file named "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" in the target directory.
9. Select Code entry type as "Upload a .ZIP file" and then upload the "alexa-skills-kit-samples-1.0-jar-with-dependencies.jar" file from the build directory to Lambda
9. Set the Handler as amazon.HSBCSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the zip).
10. Create a basic execution role and click create.
11. Leave the Advanced settings as the defaults.
12. Click "Next" and review the settings then click "Create Function"
13. Click the "Event Sources" tab and select "Add event source"
14. Set the Event Source type as Alexa Skills kit and Enable it now. Click Submit.
15. Copy the ARN from the top right to be used later in the Alexa Skill Setup.

### Alexa Skill Setup
1. Go to the [Alexa Console](https://developer.amazon.com/edw/home.html) and click Add a New Skill.
2. Set "HSBC" as the skill name and "my bank" as the invocation name, this is what is used to activate your skill. For example you would say: "Alexa, tell Greeter to say hello."
3. Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
4. Copy the Intent Schema from the included IntentSchema.json.
5. Copy the Sample Utterances from the included SampleUtterances.txt. Click Next.
6. Go back to the skill Information tab and copy the appId. Paste the appId into the config.properties file,
   then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
7. You are now able to start testing your sample skill! You should be able to go to the [Echo webpage](http://echo.amazon.com/#skills) and see your skill enabled.
8. In order to test it, try to say some of the Sample Utterances from the Examples section below.
9. Your skill is now saved and once you are finished testing you can continue to publish your skill.


## Examples
### Public question:

    User: "Alexa, ask my bank where is the nearsest agency ?"
    Alexa: "There is ona at..."
    
### Private question with authentification
    User: "Alexa, ask my bank who is my bank advisor"
    Alexa: "You need to login first. What's your login?"
    User: "My login is [login]"
    Alexa: "Welcome [user's name]. What is your password"
    User: "[Password]"
    Alexa: "Your advisor is [advisor's name]"