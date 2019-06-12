# Vista previa de la personalización

## Login

Lo primero que el usuario ve al entrar en la personalización es la siguiente vista, en la que se le
ofrece iniciar sesión.

![xl_login] ![sm_login]

Como nota curiosa, el fondo es interactivo, existen nodos con carga positiva o negativa y en base
a ello se atraerán o repelerán. El cursor también es un nodo y la acción de click invierte su carga.

## Inicio

Esta es la vista inicial de la personalización, en ella se presenta una navegación por pestañas que
muestran los archivos que el administrador y el usuario han elegido.

Por defecto, las dos únicas pestañas que aparecen son "Global" e "Inicio", en la primera es el
administrador quien elige los archivos que se muestran marcando una opción desde el buscador, y en
la segunda es el usuario quien hace esto. El administrador o el usuario pueden arrastrar en su
respectiva pestaña los archivos para que se muestren en el orden que desee.

![xl_home] ![sm_home]

El resto de pestañas de ejemplo que se ven en la imagen muestran los archivos del repositorio que
contienen una etiqueta con el mismo nombre que la pestaña. Estas etiquetas son definidas en el
buscador.

![xl_home_tab] ![sm_home_tab]

Estas pestañas pueden ser creadas y cambiadas de orden por el usuario, en ellas se define un nombre,
color e icono.

![xl_home_tab_new] ![sm_home_tab_new]

Como es lógico, el usuario también puede eliminarlas tras confirmar la acción.

![xl_home_tab_remove] ![sm_home_tab_remove]

Para optimizar el uso de pantalla en móviles, la barra superior y lateral son colapsables.

![sm_home_navbar_expanded] ![sm_home_sidebar_collapsed]

Como anteriormente se mencionó, existe un buscador desde el cual el usuario ve los archivos de un
directorio de forma recursiva. Puede explorar todo el árbol de directorios y filtrar los archivos en
base a unos criterios definidos en la sección superior de la vista.

![xl_stsearch] ![sm_stsearch]

El usuario puede editar los metadatos de los archivos y estos son guardados en el idioma
seleccionado (a excepción de la imagen y las etiquetas, que son idénticos independientemente del
idioma).

![xl_stsearch_edit] ![sm_stsearch_edit]

Si hay demasiados archivos estos serán paginados.

![xl_stsearch_bottom]

La siguiente vista es la del perfil del usuario, la cual puede accederse haciendo click en la imagen
de perfil o desde el menú de opciones superior.

![xl_home_navbar_options]

En ella el usuario puede actualizar su perfil, actualmente estos datos son utilizados en algunas
partes de la interfaz.

![xl_profile] ![sm_profile]

La última vista propia implementada es administración, que consiste en una colección de varios
accesos directos a distintas secciones para gestionar Pentaho BI Server.

![xl_administration] ![sm_administration]

Como en el fondo esto continúa siendo Pentaho BI Server, también se puede acceder a las perspectivas
tradicionales.

![xl_browser] ![sm_browser]

![xl_opened] ![sm_opened]

[sm_administration]: ./screenshots/sm_administration.png (Administración \(móvil\))
[sm_browser]: ./screenshots/sm_browser.png (Perspectiva de explorador \(móvil\))
[sm_home_navbar_expanded]: ./screenshots/sm_home_navbar_expanded.png (Menú superior expandido \(móvil\))
[sm_home_sidebar_collapsed]: ./screenshots/sm_home_sidebar_collapsed.png (Menú lateral colapsado \(móvil\))
[sm_home_tab_new]: ./screenshots/sm_home_tab_new.png (Crear pestaña de inicio \(móvil\))
[sm_home_tab_remove]: ./screenshots/sm_home_tab_remove.png (Eliminar pestaña de inicio \(móvil\))
[sm_home_tab]: ./screenshots/sm_home_tab.png (Pestaña de inicio \(móvil\))
[sm_home]: ./screenshots/sm_home.png (Inicio \(móvil\))
[sm_login]: ./screenshots/sm_login.png (Login \(móvil\))
[sm_opened]: ./screenshots/sm_opened.png (Perspectiva de abiertos \(móvil\))
[sm_profile]: ./screenshots/sm_profile.png (Perfil \(móvil\))
[sm_stsearch_edit]: ./screenshots/sm_stsearch_edit.png (Edición de STSearch \(móvil\))
[sm_stsearch]: ./screenshots/sm_stsearch.png (STSearch \(móvil\))
[xl_administration]: ./screenshots/xl_administration.png (Administración \(escritorio\))
[xl_browser]: ./screenshots/xl_browser.png (Perspectiva de explorador \(escritorio\))
[xl_home_navbar_options]: ./screenshots/xl_home_navbar_options.png (Opciones del menú superior \(escritorio\))
[xl_home_tab_new]: ./screenshots/xl_home_tab_new.png (Crear pestaña de inicio \(escritorio\))
[xl_home_tab_remove]: ./screenshots/xl_home_tab_remove.png (Eliminar pestaña de inicio \(escritorio\))
[xl_home_tab]: ./screenshots/xl_home_tab.png (Pestaña de inicio \(escritorio\))
[xl_home]: ./screenshots/xl_home.png (Inicio \(escritorio\))
[xl_login]: ./screenshots/xl_login.png (Login \(escritorio\))
[xl_opened]: ./screenshots/xl_opened.png (Perspectiva de abiertos \(escritorio\))
[xl_profile]: ./screenshots/xl_profile.png (Perfil \(escritorio\))
[xl_stsearch_bottom]: ./screenshots/xl_stsearch_bottom.png (Paginación de STSearch \(escritorio\))
[xl_stsearch_edit]: ./screenshots/xl_stsearch_edit.png (Edición de STSearch \(escritorio\))
[xl_stsearch]: ./screenshots/xl_stsearch.png (STSearch \(escritorio\))
