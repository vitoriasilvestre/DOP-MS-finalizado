package br.ufc.mdcc.caos.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoCursor;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/caos")
public class DataController {

	@Value("${number}")
	int number;

	@Autowired
	MongoTemplate mongoTemplate;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Document>> getAllData() {
		MongoCursor<Document> cursor = mongoTemplate.getDb().getCollection("caos").find().iterator();

		List<Document> docs = new ArrayList<Document>();
		try {
			while (cursor.hasNext()) {
				docs.add(cursor.next());
			}
		} finally {
			cursor.close();
		}

		return new ResponseEntity<List<Document>>(docs, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/filter")
	public ResponseEntity<List<Document>> getDataSpecific2(@RequestBody String inputData) {
		System.out.println(inputData);
		Query query = new BasicQuery(inputData);
		
		List<Document> docs = mongoTemplate.find(query, Document.class, "caos");		
		
		return new ResponseEntity<List<Document>>(docs, HttpStatus.OK);
	}	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addData(@RequestBody String inputData) {
		JSONArray jsonArray = new JSONArray(inputData);
		
		System.out.println(jsonArray);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			mongoTemplate.insert(jsonObj, "caos");
		}

		return new ResponseEntity<ResponseBody>(HttpStatus.OK);
	}
	
	/*

	@RequestMapping(method = RequestMethod.POST, path = "/find")
	public ResponseEntity<List<Document>> getDataSpecific(@RequestBody String inputData) {
		JSONArray jsonArray = new JSONArray(inputData);

		Bson filter = Filters.exists("_id");

		for (int i = 0; i < jsonArray.length() - 1; i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Object value = null;

			String operator = jsonObj.getString("operator");
			String field = "map." + jsonObj.getString("field");
			value = jsonObj.get("value");

			if (jsonObj.getString("type").equals("sensor")) {

			} else {
				if (operator.equals("=")) {
					filter = Filters.and(filter, Filters.eq(field, value));
				} else if (operator.equals("<")) {
					filter = Filters.and(filter, Filters.lt(field, value));
				} else if (operator.equals("<=")) {
					filter = Filters.and(filter, Filters.lte(field, value));
				} else if (operator.equals(">")) {
					filter = Filters.and(filter, Filters.gt(field, value));
				} else if (operator.equals(">=")) {
					filter = Filters.and(filter, Filters.gte(field, value));
				}
			}
		}

		int auxNumber = 0;

		if (!jsonArray.isEmpty() && !jsonArray.getString(jsonArray.length() - 1).equals("")) {
			auxNumber = jsonArray.getInt(jsonArray.length() - 1);
		} else {
			auxNumber = number;
		}

		MongoCursor<Document> cursor = mongoTemplate.getDb().getCollection("caos").find(filter).limit(auxNumber)
				.sort(new BasicDBObject("_id", -1)).iterator();

		List<Document> docs = new ArrayList<Document>();

		if (auxNumber == 0) {
			return new ResponseEntity<List<Document>>(docs, HttpStatus.OK);
		}

		try {
			int flag = 0;

			for (int i = 0; i < jsonArray.length() - 1; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				if (jsonObj.getString("type").equals("sensor")) {
					flag++;
				}
			}

			if (flag == 0) {
				while (cursor.hasNext()) {
					Document doc = (Document) cursor.next().get("map");
					docs.add(doc);
				}
			} else {

				while (cursor.hasNext()) {
					Document doc = (Document) cursor.next().get("map");

					int sizeSensor = 0;

					for (int i = 0; i < jsonArray.length() - 1; i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);

						if (jsonObj.getString("type").equals("sensor")) {
							JSONObject jsonSensor = (JSONObject) jsonObj.get("value");

							jsonSensor.getString("vType");
							jsonSensor.getString("value");
							jsonSensor.get("vValue");

							Document array = (Document) doc.get("sensors");
							List myArrayList = (List) array.get("myArrayList");

							for (Object aux : myArrayList.toArray()) {
								Document auxDoc = (Document) aux;
								Document auxMap = (Document) auxDoc.get("map");

								int j = 0;

								if (auxMap.getString("type").equals(jsonSensor.getString("vType"))) {
									j++;
								}

								String operator = jsonSensor.getString("opValue");

								if (operator.equals("=")) {
									if (auxMap.get("value") instanceof Double
											&& (jsonSensor.get("vValue") instanceof Double
													|| jsonSensor.get("vValue") instanceof Integer)) {										 
										if (auxMap.getDouble("value") == jsonSensor.getDouble("vValue")) {
											j++;
										}
									} else if (auxMap.get("value") instanceof Document
											&& jsonSensor.get("vValue") instanceof JSONArray) {
										Document array1 = (Document) auxMap.get("value");
										List<Double> jsonArray1 = (List) array1.get("myArrayList");
										JSONArray jsonArray2 = (JSONArray) jsonSensor.get("vValue");

										int l = 0;
										for (int k = 0; k < jsonArray1.size(); k++) {
											if (jsonArray1.get(k) == jsonArray2.getDouble(k)) {
												l++;
											}
										}

										if (l == jsonArray1.size()) {
											j++;
										}
									} else if (auxMap.get("value") instanceof String
											&& jsonSensor.get("vValue") instanceof String) {
										if (auxMap.getString("value").equals(jsonSensor.get("vValue"))) {
											j++;
										}
									}
								} else if (operator.equals("<")) {
									if (auxMap.get("value") instanceof Double
											&& (jsonSensor.get("vValue") instanceof Double
													|| jsonSensor.get("vValue") instanceof Integer)) {										 
										if (auxMap.getDouble("value") < jsonSensor.getDouble("vValue")) {
											j++;
										}
									} else if (auxMap.get("value") instanceof Document
											&& jsonSensor.get("vValue") instanceof JSONArray) {
										Document array1 = (Document) auxMap.get("value");
										List<Double> jsonArray1 = (List) array1.get("myArrayList");
										JSONArray jsonArray2 = (JSONArray) jsonSensor.get("vValue");

										int l = 0;
										for (int k = 0; k < jsonArray1.size(); k++) {
											if (jsonArray1.get(k) < jsonArray2.getDouble(k)) {
												l++;
											}
										}

										if (l == jsonArray1.size()) {
											j++;
										}
									}
								} else if (operator.equals("<=")) {
									if (auxMap.get("value") instanceof Double
											&& (jsonSensor.get("vValue") instanceof Double
													|| jsonSensor.get("vValue") instanceof Integer)) {										 
										if (auxMap.getDouble("value") <= jsonSensor.getDouble("vValue")) {
											j++;
										}
									} else if (auxMap.get("value") instanceof Document
											&& jsonSensor.get("vValue") instanceof JSONArray) {
										Document array1 = (Document) auxMap.get("value");
										List<Double> jsonArray1 = (List) array1.get("myArrayList");
										JSONArray jsonArray2 = (JSONArray) jsonSensor.get("vValue");

										int l = 0;
										for (int k = 0; k < jsonArray1.size(); k++) {
											if (jsonArray1.get(k) <= jsonArray2.getDouble(k)) {
												l++;
											}
										}

										if (l == jsonArray1.size()) {
											j++;
										}
									}
								} else if (operator.equals(">")) {
									if (auxMap.get("value") instanceof Double
											&& (jsonSensor.get("vValue") instanceof Double
													|| jsonSensor.get("vValue") instanceof Integer)) {										 
										if (auxMap.getDouble("value") > jsonSensor.getDouble("vValue")) {
											j++;
										}
									} else if (auxMap.get("value") instanceof Document
											&& jsonSensor.get("vValue") instanceof JSONArray) {
										Document array1 = (Document) auxMap.get("value");
										List<Double> jsonArray1 = (List) array1.get("myArrayList");
										JSONArray jsonArray2 = (JSONArray) jsonSensor.get("vValue");

										int l = 0;
										for (int k = 0; k < jsonArray1.size(); k++) {
											if (jsonArray1.get(k) > jsonArray2.getDouble(k)) {
												l++;
											}
										}

										if (l == jsonArray1.size()) {
											j++;
										}
									}
								} else if (operator.equals(">=")) {
									if (auxMap.get("value") instanceof Double
											&& (jsonSensor.get("vValue") instanceof Double
													|| jsonSensor.get("vValue") instanceof Integer)) {										 
										if (auxMap.getDouble("value") >= jsonSensor.getDouble("vValue")) {
											j++;
										}
									} else if (auxMap.get("value") instanceof Document
											&& jsonSensor.get("vValue") instanceof JSONArray) {
										Document array1 = (Document) auxMap.get("value");
										List<Double> jsonArray1 = (List) array1.get("myArrayList");
										JSONArray jsonArray2 = (JSONArray) jsonSensor.get("vValue");

										int l = 0;
										for (int k = 0; k < jsonArray1.size(); k++) {
											if (jsonArray1.get(k) >= jsonArray2.getDouble(k)) {
												l++;
											}
										}

										if (l == jsonArray1.size()) {
											j++;
										}
									}
								}

								operator = jsonSensor.getString("opTimestamp");
								Long doubleAux = auxMap.getLong("timestamp");
								Long doubleJson = jsonSensor.getLong("vTimestamp");

								if (operator.equals("=")) {
									if (doubleAux == doubleJson) {
										j++;
									}
								} else if (operator.equals(">=")) {
									if (doubleAux >= doubleJson) {
										j++;
									}
								} else if (operator.equals(">")) {
									if (doubleAux > doubleJson) {
										j++;
									}
								} else if (operator.equals("<=")) {
									if (doubleAux <= doubleJson) {
										j++;
									}
								} else if (operator.equals("<")) {
									if (doubleAux < doubleJson) {
										j++;
									}
								}

								if (j == 3) {
									sizeSensor++;
								}
							}
						}
					}

					if (flag == sizeSensor) {
						docs.add(doc);
						System.out.println("ok");
					}
				}
			}
		} finally {
			cursor.close();
		}

		return new ResponseEntity<List<Document>>(docs, HttpStatus.OK);
	}

*/
}
