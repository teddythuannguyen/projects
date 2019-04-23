using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour {

    public GameObject enemy;
    public float xMinMax;
    public float yMin;
    public int count;
    public float startWait;
    public float cloneWait;

    private bool restart;
    private bool gameOver;


    // Use this for initialization
    void Start()
    {
        gameOver = false;
        restart = false;
        StartCoroutine(Waves());

    }

    void Update()
    {
        if (restart)
        {
            if (Input.GetKeyDown(KeyCode.R))
            {
                Application.LoadLevel(Application.loadedLevel);
            }
        }
    }

    IEnumerator Waves()
    {
        while (true)
        {
            yield return new WaitForSeconds(startWait);
            for (int i = 0; i < count; i++)
            {
                Instantiate(enemy, new Vector3(Random.Range(-xMinMax, xMinMax), yMin), Quaternion.identity);
                yield return new WaitForSeconds(cloneWait);
            }
            if (gameOver)
            {                
                restart = true;
                break;
            }
        }
    }

    public void GameOver()
    {
        gameOver = true;
    }
    
}


