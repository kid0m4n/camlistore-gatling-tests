# To generate the raw files

go run generate.go -count 1000
head -50 user-files/data/files.csv > user-files/data/files_for_retrieval.csv

# To reset camlistore

./bin/camtool dbinit -user root -password  password -host localhost -dbname camli -wipe
rm -rf /ssd/camlistore/blobs/

# Check existing BLOBs

curl --user user:password http://ip-10-178-128-85.ec2.internal:3179/bs/camli/enumerate-blobs

# Required command line environment

export GATLING_ENDPOINT=http://ip-10-178-128-85.ec2.internal:3179

export GATLING_USERNAME=user
export GATLING_PASSWORD=password

# Initializing the SSD in Amazon

sudo apt-get install mdadm -y
sudo mdadm --create --verbose /dev/md/ssd --level=stripe --raid-devices=2 /dev/xvdb /dev/xvdg
sudo mkfs.ext4 /dev/md/ssd
sudo mkdir /ssd
sudo mount /dev/md/ssd /ssd
