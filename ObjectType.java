
public enum ObjectType {
	NULL, WALL, DOT, ENERGIZER;
	
	public static ObjectType getObjectType(int numVal) {
		return values()[numVal];
	}
}
