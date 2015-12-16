Crossdata Sandbox
*****************

Crossdata provides a sandbox that allows to test its functionality. This sandbox includes Crossdata 1.1.0.

Requisites
============
`Oracle Virtual Box <https://www.virtualbox.org/wiki/Downloads>`_

`Vagrant <https://www.vagrantup.com/downloads.html>`_

If you are using Windows system you will need:
`Git Console <http://git-scm.com/download/win>`_
or
`CGYWin <https://cygwin.com/install.html>`_

How to get the Crossdata sandbox (Vagrant)
============================================
Start with Crossdata is very easy. Just follow this steps::

    > mkdir CrossdataSandbox
    > cd CrossdataSandbox
    > vagrant init stratio/crossdata
    
At this point we have the vagrant file with the Crossdata configuration ready to start.::

    > vagrant up
    
Now, the sandbox is ready to run.::

    > vagrant ssh
    
Done! You can test Crossdata as you want.

When you finish your tests, you can stop it.::

    > vagrant halt
    
and you can remove the vagrant instance.::

    > vagrant destroy
    

How to get the Crossdata sandbox (OVA)
============================================
You can get the OVA file with the Sandbox from this [link](www.stratio.com/crossdata).
Once it has been downloaded, just import in your Virtual Box.


First Steps with Crossdata Sandbox
=============================================

TODO


F.A.Q about the sandbox
=======================

##### **I am in the same directory that I copy the Vagrant file but I have this error:**::


    A Vagrant environment or target machine is required to run this
    command. Run vagrant init to create a new Vagrant environment. Or,
    get an ID of a target machine from vagrant global-status to run
    this command on. A final option is to change to a directory with a
    Vagrantfile and to try again.


Make sure your file name is Vagrantfile instead of Vagrantfile.txt or VagrantFile.

______________________________________________________________________________________

##### **When I execute vagrant ssh I have this error:** ::


    ssh executable not found in any directories in the %PATH% variable. Is an
    SSH client installed? Try installing Cygwin, MinGW or Git, all of which
    contain an SSH client. Or use your favorite SSH client with the following
    authentication information shown below:


We need to install `Cygwin <https://cygwin.com/install.html>`_ or `Git for Windows <http://git-scm.com/download/win>`_.



For more information please visit `<http://stratio.github.io/crossdata>`_

