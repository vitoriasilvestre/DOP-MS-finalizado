package br.ufc.mdcc.caos.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.mdcc.caos.repository.DataRepository;

@Service
public class DataService {

	@Autowired
	DataRepository dataRepo;

	public void save(JSONObject json) {
		System.out.println(json);
		dataRepo.save(json);
	}

	public List<JSONObject> findAll() {
		return dataRepo.findAll();
	}

	public long count() {
		return dataRepo.count();
	}

	public JSONObject findById(String id) {
		return dataRepo.findById(id).get();
	}

	public void delete(String id) {
		dataRepo.deleteById(id);
	}
}
