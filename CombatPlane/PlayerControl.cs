using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Boundary
{
    public float xMin, xMax, yMin, yMax;
}

public class PlayerControl : MonoBehaviour {

    public float speed;
    public Boundary bound;
    public GameObject bullet;
    public Transform bulletSpawn;

    public float fireRate;
    public float timeRate;

    private AudioSource source;
    public AudioClip shootSound;

    void Awake()
    {
        source = GetComponent<AudioSource>();
    }

 
    void FixedUpdate()
    {
        var hoz = Input.GetAxis("Horizontal");
        var ver = Input.GetAxis("Vertical");

        GetComponent<Rigidbody>().velocity = new Vector3(hoz, ver) * speed;

        transform.position = new Vector3(
            Mathf.Clamp(transform.position.x, bound.xMin, bound.xMax),
            Mathf.Clamp(transform.position.y, bound.yMin, bound.yMax)
            );
        if (Input.GetKeyDown(KeyCode.Space) && Time.time > timeRate){
            source.PlayOneShot(shootSound, 1F);
            timeRate = Time.time + fireRate;
            Instantiate(bullet, bulletSpawn.position, Quaternion.identity);
        }
    }
}

