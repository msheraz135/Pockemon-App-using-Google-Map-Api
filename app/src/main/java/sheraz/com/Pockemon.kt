package sheraz.com

import android.location.Location

class Pockemon {
    var name: String ? = null
    var des: String ? = null
    var image: Int ? = null
    var power :Double ? = null
    var lat :Double ? = null
    var log :Double ? = null
    var location :Location ? = null
    var isCatch:Boolean = false
    constructor(
        image: Int?,
        name: String?,
        des: String?,
        power: Double?,
        lat: Double,
        log: Double
    ) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        location=Location(name)
        this.location!!.latitude=lat
        this.lat=lat
        this.location!!.longitude=log
        this.log=log
        this.isCatch=false
    }
}