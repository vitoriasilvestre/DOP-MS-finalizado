package br.ufc.great.syssu.main;

import static br.ufc.great.syssu.eval.Expression.and;
import static br.ufc.great.syssu.eval.Expression.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.ILocalDomain;
import br.ufc.great.syssu.eval.Expression;
import br.ufc.great.syssu.eval.var.StringConstant;
import br.ufc.great.syssu.eval.var.StringVariable;
import br.ufc.great.syssu.ubibroker.LocalUbiBroker;
import br.ufc.mdcc.caos.client.RetrofitConfig;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SysSUManager {

	private LocalUbiBroker mLocalUbiBroker;
	private ILocalDomain mDomain;
	private ILocalDomain mDomainDump;
	private String endpoint;
	private boolean isLocal = false; // alterar o public

	public SysSUManager(String endpoint, boolean isLocal) {
		try {
			mLocalUbiBroker = LocalUbiBroker.createUbibroker();
			mDomain = (ILocalDomain) mLocalUbiBroker.getDomain("caos");
			mDomainDump = (ILocalDomain) mLocalUbiBroker.getDomain("dump");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.endpoint = endpoint;
		this.isLocal = isLocal;
	}

	public void put(Tuple tuple) {
		try {
			mDomain.put(tuple, null);
			mDomainDump.put(tuple, null);
		} catch (TupleSpaceException | TupleSpaceSecurityException e) {
			e.printStackTrace();
		}
	}

	public List<Tuple> read(Pattern pattern, IFilter filter) {
		List<Tuple> list = null;

		if (!isLocal) {
			if (pattern.size() > 0) {
				StringVariable field = new StringVariable(pattern.getField(0).getName());
				Expression auxExp = eq(field, new StringConstant(pattern.getField(0).getValue().toString()));
				Expression finalExp = auxExp;

				for (int i = 1; i < pattern.size(); i++) {
					field = new StringVariable(pattern.getField(i).getName());
					auxExp = eq(field, new StringConstant(pattern.getField(i).getValue().toString()));
					finalExp = and(finalExp, auxExp);
				}
				
				if(filter != null) {
					if(filter.remoteFilter() != null) {
						finalExp = and(finalExp, filter.remoteFilter());						
					}	
				}				
				
				return convertJsonToTuples(filter(clearCaracters(finalExp)));
			}
		} else {
			try {
				list = mDomain.read(pattern, filter, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<Tuple> readForSync() {
		List<Tuple> list = null;

		Pattern pattern = (Pattern) new Pattern().addField("?", "?");

		try {
			list = mDomainDump.read(pattern, "", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void clear() {
		Pattern pattern = (Pattern) new Pattern().addField("?", "?");

		try {
			mDomainDump.take(pattern, "", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String clearCaracters(Expression exp) {
		String result = exp.toString().replace("\"", "");

		int firstIndex = 0;

		while (firstIndex != -1) {
			firstIndex = result.indexOf("{ 'map.sensors.myArrayList': { $elemMatch : ", firstIndex);

			try {
				if (firstIndex != -1) {
					int lastIndex1 = result.indexOf("} }", firstIndex) + 2;
					result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
					lastIndex1 = result.indexOf("} }", lastIndex1) + 2;
					result = result.substring(0, lastIndex1) + ", " + result.substring(lastIndex1 + 6);
					firstIndex += "{ 'map.sensors.myArrayList': { $elemMatch : ".length();
				}
			} catch (StringIndexOutOfBoundsException e) {
				break;
			}
		}

		return result;
	}

	private List<Tuple> convertJsonToTuples(String result) {
		List<Tuple> tuples = new ArrayList<Tuple>();

		try {
			JSONArray array = new JSONArray(result);

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Tuple tuple = (Tuple) new Tuple();

				Iterator iter = obj.keys();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					try {
						if (key.equals("_id") || key.equals("_class")) {
						} else {
							JSONObject json2 = obj.getJSONObject(key);

							Iterator iter2 = json2.keys();
							while (iter2.hasNext()) {
								String key2 = (String) iter2.next();

								if (json2.get(key2) instanceof JSONObject) {
									JSONArray array2 = json2.getJSONObject(key2).getJSONArray("myArrayList");

									for (int j = 0; j < array2.length(); j++) {
										JSONObject json3 = array2.getJSONObject(j).getJSONObject("map");

										Tuple tuple2 = new Tuple();

										Iterator iter3 = json3.keys();
										while (iter3.hasNext()) {
											String key3 = (String) iter3.next();
											tuple2.addField(key3, json3.get(key3));
										}

										tuple.addField("sensor", tuple2);
									}
								} else {
									tuple.addField(key2, json2.get(key2));
								}
							}
						}
					} catch (JSONException e) {
						
					}
				}

				tuples.add(tuple);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return tuples;
	}

	private String filter(String input) {
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), input);

		Call<ResponseBody> call = new RetrofitConfig(endpoint).getDataService().filterDatas(body);
		try {
			String result = call.execute().body().string();
			return result;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;
	}
}
