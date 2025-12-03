@urgencias
Feature: Registro de ingresos en el modulo de urgencias

  Como enfermera
  Quiero poder registrar las admisiones de los pacientes a urgencias
  Para determinar qué pacientes tienen mayor prioridad de atención


  Background:
    Given que la siguiente enfermera esta registrada:
      | Cuil          | Nombre | Apellido |
      | 20-32456878-7 | Lucia  | Paz      |
    And que los siguientes pacientes esten registrados:
      | Cuil          | Nombre   | Apellido | Calle      | Numero | Localidad | Obra Social Codigo | Obra Social Nombre | Numero Afiliado |
      | 20-40902338-0 | Facundo  | Moya     | San Martín | 1234   | Tucumán   | OSDE               | OSDE               | 45-889900       |
      | 20-43336577-2 | Santiago | Martin   | Laprida    | 321    | Tucumán   |                    |                    |                 |


  # Escenario 1
  Scenario: Ingreso de paciente existente con datos validos
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-40902338-0 | Dolor abdominal agudo | Emergencia          | 37.5        | 80                  | 20                      | 120               | 80                 | 20-32456878-7  |
    Then la lista de espera esta ordenada segun el nivel de emergencia por cuil de la siguiente manera:
      | Cuil          | Nombre  | Apellido |
      | 20-40902338-0 | Facundo | Moya     |


  # Escenario 2
  Scenario: Ingreso con datos mandatorios faltantes
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-40902338-0 | Dolor abdominal agudo | Emergencia          | 37.5        |                     | 20                      | 120               | 80                 | 20-32456878-7  |
    Then el sistema muestra un error indicando la "frecuencia cardiaca es obligatoria"

  # Escenario 3
  Scenario: Ingreso con frecuencia cardíaca negativa
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-40902338-0 | Dolor abdominal agudo | Emergencia          | 37.5        | -80                 | 20                      | 120               | 80                 | 20-32456878-7  |
    Then el sistema muestra un error indicando la "La frecuencia cardiaca no puede ser negativa"

  # Escenario 4
  Scenario: Ingreso con frecuencia respiratoria negativa
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-40902338-0 | Dolor abdominal agudo | Emergencia          | 37.5        | 80                  | -20                     | 120               | 80                 | 20-32456878-7  |
    Then el sistema muestra un error indicando la "La frecuencia respiratoria no puede ser negativa"

  # Escenario 5
  Scenario: Paciente con mayor prioridad entra después pero debe ser atendido primero
    Given la lista de espera actual es:
      | Cuil          | Nombre  | Apellido | Nivel de emergencia |
      | 20-40902338-0 | Facundo | Moya     | Urgencia            |
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-43336577-2 | Dolor abdominal agudo | Critica             | 37.5        | 80                  | 20                      | 120               | 80                 | 20-32456878-7  |
    Then el sistema prioriza al paciente por su nivel de emergencia:
      | Cuil          | Nombre   | Apellido |
      | 20-43336577-2 | Santiago | Martin   |
      | 20-40902338-0 | Facundo  | Moya     |

  # Escenario 6
  Scenario: Paciente con menor prioridad entra después y debe ser atendido después
    Given la lista de espera actual es:
      | Cuil          | Nombre  | Apellido | Nivel de emergencia |
      | 20-40902338-0 | Facundo | Moya     | Critica             |
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-43336577-2 | Dolor abdominal agudo | Urgencia            | 37.5        | 80                  | 20                      | 120               | 80                 | 20-32456878-7  |
    Then el sistema prioriza al paciente por su nivel de emergencia:
      | Cuil          | Nombre   | Apellido |
      | 20-40902338-0 | Facundo  | Moya     |
      | 20-43336577-2 | Santiago | Martin   |

  # Escenario 7
  Scenario: Dos pacientes con mismo nivel de emergencia, se prioriza por orden de llegada
    Given la lista de espera actual es:
      | Cuil          | Nombre  | Apellido | Nivel de emergencia |
      | 20-40902338-0 | Facundo | Moya     | Urgencia Menor      |
    When ingresa el siguiente paciente:
      | Cuil          | Informe               | Nivel de emergencia | Temperatura | Frecuencia cardiaca | Frecuencia respiratoria | Tension sistolica | Tension diastolica | CUIL enfermera |
      | 20-43336577-2 | Dolor abdominal agudo | Urgencia Menor      | 37.5        | 80                  | 20                      | 120               | 80                 | 20-32456878-7  |
    Then el sistema prioriza al paciente por su nivel de emergencia:
      | Cuil          | Nombre   | Apellido | Nivel de emergencia |
      | 20-40902338-0 | Facundo  | Moya     | Urgencia Menor      |
      | 20-43336577-2 | Santiago | Martin   | Urgencia Menor      |

