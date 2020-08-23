# Gradle is terrible at fetching artifacts from non-maven/ivy locations
# Sincle JOGL 2.4 is not released, and since it is necessary to fix
# windowing on Catalina, we fetch these libs
download:
	mkdir -p libs
	curl https://jogamp.org/deployment/v2.4.0-rc-20200307/jar/gluegen.jar -o libs/gluegen.jar
	curl https://jogamp.org/deployment/v2.4.0-rc-20200307/jar/gluegen-rt.jar -o libs/gluegen-rt.jar
	curl https://jogamp.org/deployment/v2.4.0-rc-20200307/jar/jogl-all.jar -o libs/jogl-all.jar
	curl https://jogamp.org/deployment/v2.4.0-rc-20200307/jar/gluegen-rt-natives-macosx-universal.jar -o libs/gluegen-rt-natives-macosx-universal.jar
	curl https://jogamp.org/deployment/v2.4.0-rc-20200307/jar/jogl-all-natives-macosx-universal.jar -o libs/jogl-all-natives-macosx-universal.jar
