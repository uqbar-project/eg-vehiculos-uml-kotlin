package ar.edu.unsam.algo2.logistica

class Vehiculo(var perfil: PerfilDeRuta) {
    var puntosDeDesgasteAcumulados = 0

    fun transportarCarga(bulto: Bulto) {
        val capacidadActual = perfil.factorCarga(this)
        puntosDeDesgasteAcumulados += bulto.puntosDeEsfuerzo(capacidadActual)
    }

}

interface PerfilDeRuta {
    fun factorCarga(vehiculo: Vehiculo): Int
}

class RutaAsfaltada : PerfilDeRuta {
    override fun factorCarga(vehiculo: Vehiculo) = 100
}

class RutaRipio : PerfilDeRuta {
    override fun factorCarga(vehiculo: Vehiculo) = 80 - (vehiculo.puntosDeDesgasteAcumulados / 1000)
}

abstract class Bulto(val valorAsegurado: Int) {
    fun puntosDeEsfuerzo(capacidadVehiculo: Int): Int {
        return (capacidadVehiculo * multiplicadorDeDificultad()) + riesgoEconomico()
    }

    abstract fun multiplicadorDeDificultad(): Int
    open fun riesgoEconomico() = valorAsegurado / 100
}

class CajaEstandar(val gramosDePeso: Int, valorAsegurado: Int) : Bulto(valorAsegurado) {
    override fun multiplicadorDeDificultad() = if (gramosDePeso > 5000) 3 else 1
}

class MaterialPeligroso(valorAsegurado: Int) : Bulto(valorAsegurado) {
    override fun multiplicadorDeDificultad() = 10
    override fun riesgoEconomico() = valorAsegurado / 10 // El riesgo es mayor
}

class CargaAgrupada(valorAsegurado: Int) : Bulto(valorAsegurado) {
    val elementos = mutableListOf<Bulto>()

    override fun multiplicadorDeDificultad() = elementos.sumOf { it.multiplicadorDeDificultad() }
    override fun riesgoEconomico() = super.riesgoEconomico() + (elementos.size * 5)
}