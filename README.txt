(This instalation notes were copied from http://hg.ruilopes.com/developerreactions)

# Install (on Ubuntu)

Make sure the paths for the binary are correctly set:

	vim upstart-job.conf

Verify that the upstart job is OK:

	init-checkconf upstart-job.conf

Install the upstart init script:

	sudo cp upstart-job.conf /etc/init/ScalaSprayExample.conf
	sudo initctl reload-configuration
	sudo initctl list | grep ScalaSprayExample
	sudo initctl status ScalaSprayExample

Start it right now:

	sudo initctl start ScalaSprayExample
