const Alexa = require('ask-sdk-core');
const operations = require('./operations.js');

const LaunchRequestHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type === 'LaunchRequest';
    },
    handle(handlerInput) {
        return GetWelcomeHandler.handle(handlerInput);
    }
};

const GetWelcomeHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type === 'IntentRequest'
            && handlerInput.requestEnvelope.request.intent.name === 'getWelcome';
    },
    async handle(handlerInput) {
        return operations.countItem()
        .then((data) => {
            if(`${data}` == 0){
                var speechText = `Bienvenido, actualmente no dispongo de los datos necesarios. Inicie primero la aplicación y vuelva a intentarlo.`
                return handlerInput.responseBuilder
                        .speak(speechText)
                        .getResponse();
            }else{
                var speechText = `Bienvenido a la Skill de AlexaGeoApp. Para verificar si podemos procesar peticiones diga: estoy autenticado. Si la autenticación es satisfactoria puede pedirme una curiosidad.`
                return handlerInput.responseBuilder
                        .speak(speechText)
                        .reprompt(speechText)
                        .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "Hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const GetDataHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type == 'IntentRequest'
        && handlerInput.requestEnvelope.request.intent.name == 'getData';
    },
    async handle(handlerInput){
        return operations.getData()
        .then((data) => {
            if(`${data}` == "true"){
                var speechText = `Actualmente está autenticado podemos resolver las peticiones. Si quiere comprobarlo diga: dime una curiosidad`
                return handlerInput.responseBuilder
                .speak(speechText)
                .reprompt(speechText)
                .getResponse();
            }else{
                var speechText = `Error en la autenticación, no es posible resolver las peticiones`
                return handlerInput.responseBuilder
                .speak(speechText)
                .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "Hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const GetCuriosityHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type == 'IntentRequest'
        && (handlerInput.requestEnvelope.request.intent.name == 'getCuriosity'
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.YesIntent'   // <-- Cogemos otra curiosidad si el usuario responde si a nuestras preguntas.
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.NextIntent');// <-- Necesario para coger otra curiosidad si el usuario dice siguiente curiosidad o similar.
    },
    async handle(handlerInput){
        return operations.getData()
        .then((data) => {
            if(`${data}` == "true"){
                var speechText = getRandomItem(CURIOSIDADES);
                return handlerInput.responseBuilder
                .speak(speechText + getRandomItem(PREGUNTAS))
                .reprompt(getRandomItem(PREGUNTAS))
                .getResponse();
            }else{
                var speechText = `Error en la autenticación, actualmente no está autenticado. No voy a resolver su petición`
                return handlerInput.responseBuilder
                .speak(speechText)
                .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "Hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const CancelAndStopHandler = {
    canHandle(handlerInput){
        return handlerInput.requestEnvelope.request.type == 'IntentRequest'
        && ( handlerInput.requestEnvelope.request.intent.name === 'AMAZON.NoIntent'
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.CancelIntent'  
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.StopIntent');
    },
    async handle(handlerInput){
        return operations.getData()
        .then((data) => {
            if(`${data}` == "true"){
                const speechText = "De acuerdo, nos vemos!"
                return handlerInput.responseBuilder
                .speak(speechText)
                .getResponse();
            }else{
                var speechText = `Error en la autenticación, actualmente no está autenticado. No voy a resolver su petición`
                return handlerInput.responseBuilder
                .speak(speechText)
                .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "Hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const HelpIntentHandler = {
    canHandle(handlerInput){
        return handlerInput.requestEnvelope.request.type == 'IntentRequest'
        && handlerInput.requestEnvelope.request.intent.name === 'AMAZON.HelpIntent';
    },
    async handle(handlerInput){
        return operations.getData()
        .then((data) => {
            if(`${data}` == "true"){
                const speechText = "Cuando la autenticación sea satisfactoria puedes pedirme una curiosidad. Para comprobar el estado de la autenticación diga: estoy autenticado."
                return handlerInput.responseBuilder
                .speak(speechText)
                .reprompt(speechText)
                .getResponse(); 
            }else{
                var speechText = `Lo siento, no puedo ayudarle, no está autenticado correctamente`
                return handlerInput.responseBuilder
                .speak(speechText)
                .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "Hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const ErrorHandler = {
    canHandle() {
        return true;
    },
    handle(handlerInput, error) {
        console.log(`~~~~ Error handled: ${error.message}`);
        const speechText = 'Perdona, hubo un error. Por favor inténtalo otra vez';

        return handlerInput.responseBuilder
            .speak(speechText)
            .reprompt(speechText)
            .getResponse();
    }
};

function getRandomItem(array){
    return array[Math.floor(Math.random()*array.length)]
}

const CURIOSIDADES = [
'El 95% de los ataques cibernéticos se deben a errores humanos. ',
'Un ataque de phising ocurre cada 30 segundos. ',
'Los hackers tardan en promedio 191 días en detectar una violación de seguridad. ',
'El ransomware es el tipo de ataque cibernético más costoso, con un costo promedio de recuperación de 1.85 millones de dólares. ',
'El robo de los datos personales es una de las principales preocupaciones de los usuarios en la era digital. ',
'Las contraseñas débiles son la principal causa de violaciones de seguridad. ',
'La ingeniería social es una técnica comúnmente utilizada por los hackers para engañar a las personas y obtener información confidencial. ',
'La inteligencia artificial y el aprendizaje automático son cada vez más utilizados para mejorar la seguridad en línea. '
];

const PREGUNTAS = [
'Quieres otra?',
'Quieres otra curiosidad?',
'Te gustaría saber más?',
'Quieres saber la siguiente?',
'Quieres la siguiente curiosidad?',
'Te digo otra?',
'Te digo la siguiente?',
'Te digo otra curiosidad?',
'Te digo la siguiente curiosidad?'
];

exports.handler = Alexa.SkillBuilders.custom()
    .addRequestHandlers(
        LaunchRequestHandler,
        GetWelcomeHandler,
        GetDataHandler,
        GetCuriosityHandler,
        CancelAndStopHandler,
        HelpIntentHandler)
    .addErrorHandlers(
        ErrorHandler)
    .withApiClient(new Alexa.DefaultApiClient())
    .lambda();