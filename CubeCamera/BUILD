package(default_visibility = ["//visibility:public"])

java_library(
	name = "libcamera",
	srcs = [
		"src/CameraPlane.java",
		"src/CubeStuff.java",
		"src/Line.java",
		"src/Vector.java",
	]
)

java_binary(
	name = "CubeCamera",
	srcs = ["src/CubeCamera.java"],
	main_class = "CubeCamera",
	deps = ["libcamera"],
)
