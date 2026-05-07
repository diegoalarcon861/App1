package com.example.application
import com.example.application.data.Scheduler
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.application.data.DAO.userDAO
import com.example.application.data.Usuario
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class UserTest {  // ✅ Nombre en PascalCase
    private lateinit var db: Scheduler
    private lateinit var dao: userDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Scheduler::class.java)
            .allowMainThreadQueries() // ✅ Solo para tests
            .build()
        dao = db.userDao()
    }

    @After
    fun teardown() {
        db.close() // ✅ Limpia la BD en memoria después de CADA test
    }

    @Test
    fun insertarYConsultar() = runTest {
        val user = Usuario(
            idUser = 0,
            name = "Ana",
            email = "ana@test.com",
            password = "pwd",
            descrip = "Test",
            image = "url"
        )
        val idUserTest = dao.insertar(user)
        val result = dao.consulta("ana@test.com")
        val userConId = user.copy(idUser = idUserTest)
        dao.eliminar(userConId)

        assertNull(dao.consulta("x@t.com"))


        assertNotNull("La consulta no debe ser null", result)
        assertEquals("Ana", result?.name)
        assertEquals("ana@test.com", result?.email)
    }

    @Test
    fun insertarVariedadYVerificar() = runTest {
        val users = listOf(
            Usuario(name = "A", email = "a@t.com", password = "1", descrip = "", image = ""),
            Usuario(name = "B", email = "b@t.com", password = "2", descrip = "", image = "")
        )
        dao.insertarVariedad(users)

        assertNotNull(dao.consulta("a@t.com"))
        assertNotNull(dao.consulta("b@t.com"))
    }

    @Test
    fun eliminarUsuario() = runTest {  // ✅ Sin ": Unit"
        val user = Usuario(name = "X", email = "x@t.com", password = "1", descrip = "", image = "")
        dao.insertar(user)

        // Verificar que se insertó correctamente
        assertNotNull(dao.consulta("x@t.com"))

        dao.eliminar(user)

        // ✅ CORREGIDO: Después de eliminar debe ser null
        assertNull("Tras eliminar, la consulta debe retornar null", dao.consulta("x@t.com"))
    }
}