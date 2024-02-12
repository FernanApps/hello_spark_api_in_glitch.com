# Documentación de la API Horóscopo

## Horóscopo - Carta Astral 

### Códigos de Paises
- **Ruta:** `/horoscope-astral-chart-country`
- **Método:** GET

**Ejemplo de respuesta:** 
```json
	[
	  {
	    "id": 100,
	    "name": "ESPAÑA"
	  },
	  {
	    "id": 5,
	    "name": "ARGENTINA"
	  },
	  ....
	]
```


Esta ruta devuelve todos los codigo de paises

### Códigos de Estados

- **Ruta:** `/horoscope-astral-chart-state`
- **Método:** POST
- **Parámetros:**
  - `codeCountry`: Código del país

Este endpoint devuelve todos los codigo de estados de un pais. 

**Ejemplo de solicitud:**

```bash
curl --location '/horoscope-astral-chart-state' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'codeCountry=87'
```
**Ejemplo de respuesta:**
```json
	[
	  {
	    "id": 496,
	    "name": "Amazonas"
	  },
	  {
	    "id": 497,
	    "name": "Ancash"
	  },
	  ...
	]
```

### Códigos de Lugar

- **Ruta:** `/horoscope-astral-chart-location`
- **Método:** POST
- **Parámetros:**
  - `codeCountry`: Código del país
  - `codeState`: Código del estado seleccionado

Este endpoint devuelve todos los codigo de los lugares de un estado de un pais. 

**Ejemplo de solicitud:**

```bash
curl --location 'http://localhost:4567/horoscope-astral-chart-location' \  
--header 'Content-Type: application/x-www-form-urlencoded' \  
--data-urlencode 'codeCountry=7' \  
--data-urlencode 'codeState=10' \
```
**Ejemplo de respuesta:**
```json
	[
	  {
	    "id": 247829,
	    "name": "Acachare"
	  },
	  {
	    "id": 247831,
	    "name": "Acacoto"
	  },
	  ...
	]
```

### Carta Astral

- **Ruta:** `/horoscope-astral-chart`
- **Método:** POST
- **Descripción:** Este endpoint devuelve la carta astral para una fecha y hora específicas, en una ubicación determinada dentro de un país y estado especificados.
- **Parámetros:**
  - `codeCountry`: Código del país
  - `codeState`: Código del estado
  - `codeLocation`: Código de la ubicación
  - `day`: Día
  - `month`: Mes
  - `year`: Año
  - `hour`: Hora
  - `minutes`: Minutos

**Ejemplo de solicitud:**

```bash
curl --location '/horoscope-astral-chart' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'codeCountry=7' \
--data-urlencode 'codeState=50' \
--data-urlencode 'codeLocation=292219' \
--data-urlencode 'day=14' \
--data-urlencode 'month=12' \
--data-urlencode 'year=1947' \
--data-urlencode 'hour=10' \
--data-urlencode 'minutes=45'
```
**Ejemplo de respuesta:**
```json
	{
	  "image": "https://example.com/image.img",
	  "params": {
	    "Nacimiento": "Martes 4 Enero 1997",
	    "Lugar": "Várzea Redonda - Beja - Portugal",
	    "Long": "08W30 Lat:37N34",
	    "Hora nacimiento": "20:45",
	    "Tiempo Universal": "19:45",
	    "Signo Solar": "Capricornio",
	    "Signo Lunar": "Aries",
	    "Signo Ascendente": "Leo",
	    "Fortuna": "Geminis",
	    "Lilith": "Leo"
	  }
	}
```
# Working....