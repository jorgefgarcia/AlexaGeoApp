var AWS = require("aws-sdk");
AWS.config.update({region: 'us-east-1'});
//var ddb = new AWS.DynamoDB.DocumentClient();
var ddb = new AWS.DynamoDB({apiVersion: 'lasttet'});

var operations = function () { };

operations.prototype.getData = () =>{
	return new Promise((resolve,reject)=>{
		const params = {
			TableName: "tablaTutorial",
			KeyConditionExpression: "id = :id",
			ExpressionAttributeValues: {
			":id": {N: '100' }
			},
			ProjectionExpression: 'inRange',
			ScanIndexForward: false,
			Limit: 1
		};
		ddb.query(params, (err,data)=>{
			if(err){
				console.error("Hubo un error al coger los datos", JSON.stringify(err,null,2));
				return reject(JSON.stringify(err,null,2))
			}
			console.log("Los datos son:", JSON.stringify(data.Items[0].inRange.BOOL,null,2));
			resolve(data.Items[0].inRange.BOOL)
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

