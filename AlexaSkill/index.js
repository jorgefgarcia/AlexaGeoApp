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
            && (handlerInput.requestEnvelope.request.intent.name === 'getWelcome'
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.YesIntent'   // <-- necessary so the user gets another fact when it says 'yes' when asked if they want another one
            || handlerInput.requestEnvelope.request.intent.name === 'AMAZON.NextIntent');// <-- necessary so the gets another fact when it says 'next' (you might want to extend the utterances of this intent with e.g. 'next fact' and similar)
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
                var speechText = `Bienvenido a la Skill de AlexaGeoApp. Para verificar si podemos procesar peticiones diga: estoy autenticado.`
                return handlerInput.responseBuilder
                        .speak(speechText)
                        .reprompt(speechText)
                        .getResponse();
            }
        })
        .catch((err)=>{
            const speechText = "hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
};

const GetDataHandler = {
    canHandle(handlerInput) {
        return handlerInput.requestEnvelope.request.type == 'IntentRequest'
        && (handlerInput.requestEnvelope.request.intent.name == 'getData');
    },
    async handle(handlerInput){
        return operations.getData()
        .then((data) => {
            if(`${data}` == "true"){
                var speechText = `Actualmente está autenticado podemos resolver las peticiones`
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
            /*return handlerInput.responseBuilder
            .speak(speechText)
            .reprompt(speechText)
            .getResponse();*/
        })
        .catch((err)=>{
            const speechText = "hubo un error al coger el dato";
            return handlerInput.responseBuilder
            .speak(speechText)
            .getResponse();
        })
    }
}

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

exports.handler = Alexa.SkillBuilders.custom()
    .addRequestHandlers(
        LaunchRequestHandler,
        GetWelcomeHandler,
        GetDataHandler)
    .addErrorHandlers(
        ErrorHandler)
    .withApiClient(new Alexa.DefaultApiClient())
    .lambda();