package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseConcurrencyUseCase<REQUEST,RESP>{
    private val job = Job()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    val liveData = MutableLiveData<RESP>()

   abstract suspend fun  getRepoCall(param: REQUEST) : RESP


    fun request(param:REQUEST){
        scope.apply {
            launch(Dispatchers.IO +job){
                val result =  getRepoCall(param)
                liveData.postValue(result)
            }
        }
    }


}