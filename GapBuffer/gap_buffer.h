#include <stdio.h>

class GapBuffer {

    char *point;                    
    char *buffer;                   
    char *bufend;                   
    char *gapstart;                 
    char *gapend;                  
    
    unsigned int GAP_SIZE;          

    int InitBuffer(unsigned int size);

    int CopyBytes(char *destination, char *source, unsigned int length);

    void ExpandBuffer(unsigned int size);
    
    void ExpandGap(unsigned int size);
    
public:

    static const int DEFAULT_GAP_SIZE=20;

    GapBuffer(int gsize=DEFAULT_GAP_SIZE);

    GapBuffer(FILE *file, int gsize=DEFAULT_GAP_SIZE);

    GapBuffer(const GapBuffer& tb);

    ~GapBuffer();    

    int BufferSize();

    void MoveGapToPoint();
    
    void SetPoint(unsigned int offset);

    int SizeOfGap();    

    unsigned int PointOffset();

    char GetChar();

    char PreviousChar();

    void ReplaceChar(char ch);

    char NextChar();

    void PutChar(char ch);

    void InsertChar(char ch);

    void DeleteChars(unsigned int size);

    void InsertString(char *string, unsigned int length);

    void PrintBuffer();

    int SaveBufferToFile(FILE *file, unsigned int bytes);

    
};
