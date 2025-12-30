# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-diferrans1;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "e3562c3ee092e09c410fa7783b437ae2612ac7ef"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
#FILES:${PN} += "${bindir}/aesdsocket"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
EXTRA_OEMAKE += "CROSS_COMPILE=${TARGET_PREFIX}"
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile () {
	oe_runmake CC="${CC}" LD="${LD}" CFLAGS="${CFLAGS}" LDFLAGS="${LDFLAGS} ${TARGET_LDFLAGS}"
}

do_install () {
	# Required folders
	install -d ${D}${bindir}
	install -d ${D}${sysconfdir}/rcS.d 
	install -d ${D}${sysconfdir}/init.d

    # Required files for the aesdsocket
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/ 
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/init.d/
    ln -rs ${D}${sysconfdir}/init.d/aesdsocket-start-stop ${D}${sysconfdir}/rcS.d/S39aedsocket.sh
}

FILES:${PN} += "${bindir}/aesdsocket ${sysconfdir}/init.d/aesdsocket-start-stop ${sysconfdir}/rcS.d/S39aedsocket.sh"
