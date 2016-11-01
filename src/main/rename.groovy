import groovy.io.FileType

def list = []
def dir = new File("groovy")

dir.eachFileRecurse (FileType.FILES) { file ->
  list << file
}

list.each 
{
	it.renameTo it.absolutePath.split("\\.")[0] + ".java";
}