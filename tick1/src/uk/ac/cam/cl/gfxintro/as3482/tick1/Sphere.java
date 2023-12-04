package uk.ac.cam.cl.gfxintro.as3482.tick1;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	// The world-space position of the sphere
	protected Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
	}

	public Sphere(Vector3 position, double radius, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity, ColorRGB transmittance) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
		this.transmittance = transmittance;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.     
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);

		// Determine if ray and sphere intersect - if not return an empty RaycastHit
		if (b*b - 4*a*c < 0) {
			return new RaycastHit();
		}
		else {

			// If so, compute the two solutions for s
			double distanceOne = (-b + Math.sqrt(b*b - 4*a*c))/2*a;
			double distanceTwo = (-b - Math.sqrt(b*b - 4*a*c))/2*a;

			// If neither are positive, intersection occurs behind the camera
			if (distanceOne < 0 && distanceTwo < 0) {
					return new RaycastHit();
			}

			// Otherwise return a RaycastHit corresponding to the solution closest to the camera
			else if (distanceOne < distanceTwo && distanceOne >= 0){
					Vector3 intersectionPoint = ray.evaluateAt(distanceOne);
					return new RaycastHit(this, distanceOne, intersectionPoint, this.getNormalAt(intersectionPoint));
			}
			else {
				Vector3 intersectionPoint = ray.evaluateAt(distanceTwo);
				return new RaycastHit(this, distanceTwo, intersectionPoint, this.getNormalAt(intersectionPoint));
			}
		}
	}

	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 position) {
		return position.subtract(this.position).normalised();
	}
}
