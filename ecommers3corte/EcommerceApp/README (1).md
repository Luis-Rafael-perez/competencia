# 🛒 Sistema de E-Commerce en Java

Sistema de comercio electrónico funcional desarrollado en Java, aplicando los cuatro pilares de la Programación Orientada a Objetos: **herencia, encapsulación, polimorfismo y abstracción**.

---

## 📋 Descripción general

La aplicación permite a clientes explorar un catálogo de productos, gestionar un carrito de compras y realizar pedidos con distintos métodos de pago. Los administradores pueden gestionar el inventario, los pedidos y los usuarios registrados. Todos los datos se persisten en archivos CSV que sobreviven al cierre de la aplicación.

---

## 🧱 Arquitectura del proyecto

El proyecto está organizado en cuatro capas:

```
src/
├── Main.java                              ← Punto de entrada
├── modelo/                                ← Entidades del dominio (POO)
│   ├── usuario/
│   │   ├── Usuario.java                   ← Clase abstracta (ABSTRACCIÓN)
│   │   ├── Cliente.java                   ← Hereda de Usuario (HERENCIA)
│   │   └── Administrador.java             ← Hereda de Usuario (HERENCIA)
│   ├── producto/
│   │   ├── Producto.java                  ← Clase abstracta (ABSTRACCIÓN)
│   │   ├── ProductoFisico.java            ← Hereda de Producto (HERENCIA)
│   │   └── ProductoDigital.java           ← Hereda de Producto (HERENCIA)
│   ├── pago/
│   │   ├── Pago.java                      ← Interfaz (ABSTRACCIÓN)
│   │   ├── PagoTarjeta.java               ← Implementa Pago (POLIMORFISMO)
│   │   └── PagoEfectivo.java              ← Implementa Pago (POLIMORFISMO)
│   ├── carrito/
│   │   ├── Carrito.java                   ← Gestiona ítems con ArrayList
│   │   └── ItemCarrito.java               ← Ítem individual del carrito
│   ├── pedido/
│   │   └── Pedido.java                    ← Pedido confirmado con estado
│   └── excepciones/
│       ├── StockInsuficienteException.java
│       └── CredencialesInvalidasException.java
├── servicio/                              ← Lógica de negocio
│   ├── UsuarioService.java                ← Registro, login, modificar y eliminar usuarios
│   ├── ProductoService.java               ← CRUD de productos
│   ├── CarritoService.java                ← Gestión del carrito
│   └── PedidoService.java                 ← Flujo completo del pedido
├── persistencia/                          ← Lectura y escritura en archivos CSV
│   ├── GestorArchivos.java                ← Lee y escribe archivos de texto
│   ├── GestorPersistencia.java            ← Coordina carga y guardado general
│   ├── UsuarioRepositorio.java            ← usuarios.csv
│   ├── ProductoRepositorio.java           ← productos.csv
│   └── PedidoRepositorio.java             ← pedidos.csv
└── vista/                                 ← Interfaz gráfica con Swing
    ├── AppContext.java                    ← Contexto compartido entre ventanas
    ├── Estilos.java                       ← Paleta de colores y estilos de botones
    ├── VentanaLogin.java                  ← Inicio de sesión
    ├── VentanaRegistro.java               ← Registro de nuevos clientes
    ├── VentanaCatalogo.java               ← Catálogo + carrito (vista cliente)
    ├── VentanaPago.java                   ← Selección de método de pago
    ├── VentanaHistorial.java              ← Historial de pedidos del cliente
    └── VentanaAdmin.java                  ← Panel de administración
```

---

## ✅ Conceptos de POO aplicados

### Herencia
- `Usuario` (abstracta) → `Cliente` y `Administrador`
- `Producto` (abstracta) → `ProductoFisico` y `ProductoDigital`

### Encapsulación
- Todos los atributos de las entidades son `private`
- Acceso controlado mediante `getters` y `setters`

### Polimorfismo
- `getRol()` y `getResumen()` con `@Override` en `Cliente` y `Administrador`
- `procesarPago()` con `@Override` en `PagoTarjeta` y `PagoEfectivo`
- `getTipoProducto()` y `getDetalleEspecifico()` con `@Override` en subclases de `Producto`

### Abstracción
- Clase abstracta `Usuario` con método abstracto `getRol()`
- Clase abstracta `Producto` con métodos abstractos `getTipoProducto()` y `getDetalleEspecifico()`
- Interfaz `Pago` con métodos `procesarPago()`, `getMetodo()` y `getDescripcionPago()`

### Colecciones
- `ArrayList<Producto>` en `ProductoService`
- `ArrayList<Usuario>` en `UsuarioService`
- `ArrayList<Pedido>` en `PedidoService`
- `ArrayList<ItemCarrito>` en `Carrito`

### Manejo de excepciones
- `StockInsuficienteException` al agregar productos al carrito
- `CredencialesInvalidasException` al iniciar sesión
- Bloques `try/catch` en todas las operaciones de archivo, modificación y validación de datos

---

## 🖥️ Funcionalidades del sistema

### Cliente
- Registro e inicio de sesión
- Explorar y buscar productos en el catálogo
- Agregar, eliminar y modificar cantidades en el carrito
- Calcular el total en tiempo real
- Pagar con tarjeta de crédito o efectivo
- Consultar historial de pedidos con detalle

### Administrador
- Iniciar sesión con rol especial
- **Gestión de productos:** agregar, editar y eliminar productos del catálogo
- **Gestión de pedidos:** ver todos los pedidos y cambiar su estado
- **Gestión de usuarios:** ver, modificar y eliminar clientes registrados
  - Solo el administrador tiene acceso a esta funcionalidad
  - No es posible eliminar ni modificar otros administradores
  - Se valida email duplicado al modificar un cliente

---

## 💾 Persistencia de datos

Los datos se guardan automáticamente en archivos CSV dentro de la carpeta `datos/`, que se crea al ejecutar la aplicación por primera vez:

| Archivo | Contenido | Formato |
|---|---|---|
| `datos/usuarios.csv` | Clientes y administradores | `id,tipo,nombre,email,password,extra1,extra2` |
| `datos/productos.csv` | Catálogo con stock | `id,tipo,nombre,descripcion,precio,stock,categoria,...` |
| `datos/pedidos.csv` | Historial de pedidos | `id,idCliente,total,estado,metodo,fecha,direccion,items` |

Los datos se cargan al iniciar la app y se guardan cada vez que ocurre un cambio.

---

## 🔐 Control de acceso por roles

| Funcionalidad | Cliente | Administrador |
|---|:---:|:---:|
| Ver catálogo | ✅ | — |
| Gestionar carrito | ✅ | — |
| Realizar pedidos | ✅ | — |
| Ver historial propio | ✅ | — |
| Gestionar productos | ❌ | ✅ |
| Ver todos los pedidos | ❌ | ✅ |
| Cambiar estado de pedidos | ❌ | ✅ |
| **Modificar clientes** | ❌ | ✅ |
| **Eliminar clientes** | ❌ | ✅ |

---

## 🚀 Instrucciones de ejecución

### Requisitos
- Java 11 o superior
- NetBeans IDE (recomendado) o cualquier IDE compatible con Java

### Pasos en NetBeans
1. Crear un nuevo proyecto: **File → New Project → Java Application**
2. Extraer el ZIP descargado
3. Copiar las carpetas `modelo/`, `servicio/`, `persistencia/`, `vista/` y el archivo `Main.java` dentro de la carpeta `src/` del proyecto
4. Clic derecho en el proyecto → **Clean and Build**
5. Ejecutar con **Run** o `F6`

### Credenciales de prueba

| Rol | Email | Contraseña |
|---|---|---|
| Administrador | admin@tienda.com | admin123 |
| Cliente | juan@email.com | 1234 |

---

## 👥 Integrantes del grupo

| Nombre | Rol |
| oscar montes| desarrollador|
| miguel orozco| desarrollador|
| rafael perez| desarrollador|
| cristian cassiani| desarrollador |

---

## 📚 Tecnologías utilizadas

- **Lenguaje:** Java 11+
- **Interfaz gráfica:** Java Swing
- **Persistencia:** Archivos CSV (Java I/O estándar)
- **IDE:** NetBeans
