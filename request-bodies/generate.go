package main

import (
	"crypto/rand"
	"crypto/sha1"
	"encoding/hex"
	"flag"
	"fmt"
	"io"
	"io/ioutil"
	"os"
	"runtime"
	"sync"
)

var (
	count = flag.Int("count", 1, "Total files to generate")
	size  = flag.Int("size", 10, "Size of individual file in MB")
)

func main() {
	runtime.GOMAXPROCS(runtime.NumCPU())
	flag.Parse()
	sizeInBytes := *size * 1024 * 1024
	fmt.Printf("Generating %v file(s) of %v MB each\r\n", *count, *size)

	wg := &sync.WaitGroup{}
	wg.Add(*count)
	datafiles := make(chan datafile, *count)
	for i := 0; i < *count; i++ {
		go generate(i, sizeInBytes, wg, datafiles)
	}
	wg.Wait()
	csvFile, err := os.Create("files_for_upload.csv")
	if err != nil {
		panic(err)
	}
	defer csvFile.Close()
	csvFile.WriteString(fmt.Sprintln("sha,filename"))
	for i := 0; i < *count; i++ {
		datafile := <-datafiles
		csvFile.WriteString(fmt.Sprintf("%v,%v\r\n", datafile.sha, datafile.name))
	}

	fmt.Println("All files created")
}

type datafile struct {
	sha  string
	name string
}

func generate(i int, size int, wg *sync.WaitGroup, datafiles chan<- datafile) {
	b := make([]byte, size)
	n, err := io.ReadFull(rand.Reader, b)
	if n != len(b) || err != nil {
		panic(err)
	}
	h := sha1.New()
	h.Write(b)
	sha := h.Sum(nil)
	name := fmt.Sprintf("%v.file", i+1)
	ioutil.WriteFile(name, b, os.ModePerm)
	datafiles <- datafile{sha: hex.EncodeToString(sha), name: name}
	wg.Done()
}
