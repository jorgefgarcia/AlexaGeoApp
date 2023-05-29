var AWS = require("aws-sdk");
var moment = require("moment-timezone");

AWS.config.update({region: 'us-east-1'});
//var ddb = new AWS.DynamoDB.DocumentClient();
var ddb = new AWS.DynamoDB({apiVersion: 'lasttet'});

//ventana de validez
var windowTime = 500 //500 son 5 minutos en esta conversión "YYYYMMDDHHmmss"

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
			var currentDate = parseInt(dateNow.replace(/[^0-9]/g, ''));
			var numbercheck = currentDate - data.Items[0].date.N;
			var checkBool = data.Items[0].inRange.BOOL;
			if (checkBool == false){
				resolve(false);
			}else{
				if(numbercheck < windowTime){
					resolve(true);
				}else{
					resolve(false);
				}
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


module.exports = new operations();

