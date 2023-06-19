var AWS = require("aws-sdk");
var moment = require("moment-timezone");

AWS.config.update({region: 'us-east-1'});
var ddb = new AWS.DynamoDB({apiVersion: 'lasttet'});

//ventana de validez
var windowTime = 17 //17 minutos

var operations = function () { };

operations.prototype.getData = () =>{
	return new Promise((resolve,reject)=>{
		const params = {
			TableName: "tablaTutorial",
			KeyConditionExpression: "id = :id",
			ExpressionAttributeValues: {
			":id": {N: '100' }
			},
			ScanIndexForward: false,
			Limit: 1
		};
		ddb.query(params, (err,data)=>{
			if(err){
				console.error("Hubo un error al coger los datos", JSON.stringify(err,null,2));
				return reject(JSON.stringify(err,null,2))
			}
			var dateNow = moment().tz("Europe/Madrid").format('YYYY-MM-DD HH:mm:ss');
			var currentDate = dateNow.replace(/[^0-9]/g, '');
			var checkBool = data.Items[0].inRange.BOOL;
			if (checkBool == false){
				resolve(false);
			}else{
				resolve(calculoTiempoEntreFechas(currentDate, data.Items[0].date.N.toString()));
			}
		})
	});
}

operations.prototype.countItem = () => {
	return new Promise((resolve, reject) =>{
		const params = {
			TableName: "tablaTutorial"
		}
		ddb.scan(params, (err,data) =>{
			if(err){
				console.error("Hubo un error al verificar los items de la tabla", JSON.stringify(err,null,2));
				return reject(JSON.stringify(err,null,2))
			}
			console.log("La cantidad de items son:", data.Count);
			resolve(data.Count);
		})
	})
}


function calculoTiempoEntreFechas(fechaActual, fechaAlmacenada){
	var minutos = 1000 * 60;
	var fecha1 = new Date(fechaActual.substr(0,4),fechaActual.substr(4,2),fechaActual.substr(6,2), parseInt(fechaActual.substr(8,2))+2,fechaActual.substr(10,2),fechaActual.substr(12,2));
	var fecha2 = new Date(fechaAlmacenada.substr(0,4),fechaAlmacenada.substr(4,2),fechaAlmacenada.substr(6,2), parseInt(fechaAlmacenada.substr(8,2))+2,fechaAlmacenada.substr(10,2),fechaAlmacenada.substr(12,2));
	diff = fecha1.getTime() - fecha2.getTime();
	var minutosDiff = diff / minutos;
	if(minutosDiff < windowTime){
		return true;
	}else{
		return false;
	}
}


module.exports = new operations();

