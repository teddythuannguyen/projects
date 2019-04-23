using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GlobalFlock : MonoBehaviour {


	public GameObject birdPrefab;
	public GameObject goalPrefab;
	public static int tankSize = 30;

	static int numBird = 10;
	public static GameObject[] allBird = new GameObject[numBird];

	public static Vector3 goalPos = Vector3.zero;
	// Use this for initialization
	void Start () {		
		for(int i = 0; i < numBird; i++)
		{
			Vector3 pos = new Vector3(Random.Range(-tankSize,tankSize),
				Random.Range(0,tankSize),
				Random.Range(-tankSize,tankSize));
			allBird[i] = (GameObject) Instantiate(birdPrefab, pos, Quaternion.identity);
		}		
	}


	
	// Update is called once per frame
	void Update () {
		if (Random.Range (0, 10000) < 40) {
			goalPos = new Vector3 (Random.Range (-25, 25),
				0, Random.Range (-25, 25));
			goalPrefab.transform.position = goalPos;
		}		
	}
}
