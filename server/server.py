
import socket
import SocketServer
import json
import threading
import pylcdlib
import time

CONTEXT = {
        'ITEM1' : list('hi raspberry'),
        'ITEM2' : list('hello world'),
        'STR1'  : 'hi raspberry',
        'STR2'  : 'hello world',
        'UPDATE': False,
        'DEBUG' : True,
}


def shift(seq, n):
    return seq[n:] + seq[:n]


class TCPHandler(SocketServer.BaseRequestHandler):
    """ TCP socket handler """

    #override
    def handle(self):
        global CONTEXT
        data = self.request.recv(1024).strip()
        cur_thread = threading.current_thread()
        print "{} wrote:".format(cur_thread.name, data)
        CONTEXT.update(json.loads(data))
        CONTEXT['UPDATE'] = True
        time.sleep(1)


class ThreadedTCPHandler(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass


class ThreadLCDHandler(threading.Thread):
    """ LCD Handler """

    def __init__(self):
        self._lcd = pylcdlib.lcd(0x21,0)

    def load_new_string(self):
        """ load new string as list """
        global CONTEXT
        self._lcd.lcd_puts("Loading new string ....",1)    #display on line 1
        self._lcd.lcd_puts("Please wait ...",2)            #display on line 2
        CONTEXT['STR1'] += " "*20
        CONTEXT['STR2'] += " "*20
        CONTEXT['ITEM1'] = CONTEXT['STR1'].split()
        CONTEXT['ITEM2'] = CONTEXT['STR2'].split()

    def dispy_new_string(self):
        """ display """
        global CONTEXT
        self._lcd.lcd_puts("".join(CONTEXT['ITEM1']), 1)    
        self._lcd.lcd_puts("".join(CONTEXT['ITEM2']), 2)
        CONTEXT['ITEM1'] = shift(CONTEXT['ITEM1'], 1)
        CONTEXT['ITEM2'] = shift(CONTEXT['ITEM2'], 1)

    def debug_new_string(self):
        global CONTEXT
        if CONTEXT['DEBUG']:
            print CONTEXT['ITEM1']    
            print CONTEXT['ITEM2']

    def serve_forever(self):
        global CONTEXT
        while True:
            if CONTEXT['UPDATE']:
                self.load_new_string()
                self.debug_new_string()
                time.sleep(5)

            self.dispy_new_string()
            self.debug_new_string()
            time.sleep(5)


if __name__ == "__main__":
    HOST, PORT = '192.168.1.12', 5000

    thread_lcd = ThreadLCDHandler()
    thread_tcp = ThreadedTCPHandler((HOST, PORT), TCPHandler)
    ip, port = thread_tcp.server_address

    threads = [ threading.Thread(target=thread_tcp.serve_forever),
                threading.Thread(target=thread_lcd.serve_forever), ]
    [i.start() for i in threads]
    [i.join() for i in threads]

