package org.jeannyil.fuse.demo.ipservice.client.bean;

import java.io.InputStream;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONValidator {
	
	/**
	 * Levarages Everit JSON Schema validator 
	 * (https://github.com/everit-org/json-schema)
	 * @param jsonData
	 * @param jsonSchemaPath
	 * @throws Exception
	 */
	public void validateJsonWithEverit(String jsonData, String jsonSchemaPath) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonSchemaPath);
		JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
		Schema schema = SchemaLoader.load(rawSchema);
		try {
			schema.validate(new JSONObject(jsonData));
		} catch (ValidationException e) {
			String message = e.getMessage() + "\n";
			List<ValidationException> causingExceptions = e.getCausingExceptions();
			for (ValidationException causingException : causingExceptions) {
				message += causingException.getMessage() + "\n";
			}
			throw new ValidationException(schema, message, e.getKeyword());
		}
	}
	
}
