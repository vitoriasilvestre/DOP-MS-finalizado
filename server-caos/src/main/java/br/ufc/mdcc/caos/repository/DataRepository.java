package br.ufc.mdcc.caos.repository;

import org.json.JSONObject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepository extends MongoRepository<JSONObject, String> {
}
