﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Flock : MonoBehaviour {

	public float speed = 5f;
	float rotationSpeed = 5.0f;
	float neightborDistance = 10f;

	bool turning = false;

	// Use this for initialization
	void Start () {
		speed = Random.Range (1f, 2);		
	}
	
	// Update is called once per frame
	void Update () {
		if (Vector3.Distance (transform.position, Vector3.zero) >= GlobalFlock.tankSize) {
			turning = true;
		} 
		else 
			turning = false;
		
		if(turning){
			Vector3 direction = Vector3.zero - transform.position;
			transform.rotation = Quaternion.Slerp(transform.rotation,
				Quaternion.LookRotation(direction),
				rotationSpeed * Time.deltaTime);
			speed = Random.Range(0.5f, 1);
		}
		else{
			if (Random.Range (0, 5) < 1)
				ApplyRules ();			
		}
		transform.Translate (0, 0, Time.deltaTime * 5);		
	}
	void ApplyRules(){
		GameObject[] gos = GlobalFlock.allBird;

		Vector3 vcentre = Vector3.zero;
		Vector3 vavoid = Vector3.zero;
		float gSpeed = 0.1f;

		Vector3 goalPos = GlobalFlock.goalPos;

		float dist;

		int groupSize = 0;
		foreach (GameObject go in gos) {
			if (go != this.gameObject) {
				dist = Vector3.Distance (go.transform.position, this.transform.position);
				if (dist <= neightborDistance) {
					vcentre += go.transform.position;
					groupSize++;

					if (dist < 3.0f) {
						vavoid = vavoid + (this.transform.position - go.transform.position);
					}
					Flock anotherFlock = go.GetComponent<Flock> ();
					gSpeed = gSpeed + anotherFlock.speed;
				}
			}
		}

		if (groupSize > 0) {
			vcentre = vcentre / groupSize + (goalPos - this.transform.position);
			speed = gSpeed / groupSize;

			Vector3 direction = (vcentre + vavoid) - transform.position;
			if(direction != Vector3.zero)
				transform.rotation = Quaternion.Slerp(transform.rotation,
					Quaternion.LookRotation(direction),
					rotationSpeed * Time.deltaTime);				
		}
	}


}
