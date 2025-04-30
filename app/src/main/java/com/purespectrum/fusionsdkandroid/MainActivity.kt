package com.purespectrum.fusionsdkandroid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.purespectrum.fusionsdkandroid.network.RetrofitClient
import com.purespectrum.fusionsdkandroid.model.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var callButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.textView)
        callButton = findViewById(R.id.callButton)
        val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbXAiOjQ1MywiaWF0IjoxNzMwNDg1MjY5fQ.QXnLcBbX2-qBypFM7ghl5sw0-v449uHHQyLqqoYuuTE"
        val respondentId = "MS111"
        val locale = "en_US"
        val profileKey: String? = "211"
        val profileValue = "111"

        val profile = if (!profileKey.isNullOrBlank()) {
            mapOf(profileKey to profileValue)
        } else {
            emptyMap()
        }

        val call = RetrofitClient.api.getData(
            token = accessToken,
            respondentId = respondentId,
            respondentLocalization = locale,
            profileData = profile,
            surveyScoreValue = true,
            memberId = null,
            hashedId = null
        )

        callButton.setOnClickListener {
            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()

                        val display = """
                            Respondent ID: ${result?.respondentId}
                            Surveys: ${result?.surveys}
                        """.trimIndent()

                        resultText.text = display
                    } else {
                        resultText.text = "Error: ${response.code()} ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    resultText.text = "Failure: ${t.localizedMessage}"
                }
            })
        }
    }
}
